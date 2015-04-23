package com.app.data;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.model.AccStatus;
import com.app.model.Company;
import com.app.model.Entry;
import com.app.model.Event;
import com.app.model.EventAssignment;
import com.app.model.Item;
import com.app.model.Order;
import com.app.model.SubscriptionOrder;
import com.app.model.Payload;
import com.app.model.User;
import com.app.model.UserSubscription;

@Transactional
@Singleton
@Component
@SuppressWarnings("unchecked")
public class DataStoreImpl {
	@Inject
	@Autowired
	private SessionFactory sessionFactory;
	private Logger l = LoggerFactory.getLogger(DataStoreImpl.class);

	public String saveOrder(Event event) {
		final SubscriptionOrder o = new SubscriptionOrder();
		final Session s = sessionFactory.getCurrentSession();
		final Order order = event.getPayload().getOrder();
		final Payload payload = event.getPayload();
		final Set<Item> items = event.getPayload().getOrder().getItem();
		final UserSubscription userSubscription = new UserSubscription();

		s.beginTransaction();

		s.saveOrUpdate(event.getCreator());
		s.saveOrUpdate(payload.getCompany());
		o.setUserId(event.getCreator().getUuid());
		o.setCompany(payload.getCompany().getUuid());

		s.saveOrUpdate(event.getMarketplace());

		// Clear the items
		o.setItem(new LinkedHashSet<Item>());

		o.setType(event.getType());
		// Set identifier
		o.setAccountIdentifier(UUID.randomUUID().toString());
		o.setStatus(AccStatus.ACTIVE.toString());
		o.setEditionCode(order.getEditionCode());
		o.setPricingDuration(order.getPricingDuration());
		o.setMarketPlace(event.getMarketplace().getPartner());
		s.save(o);

		// Save items
		for (Item i : items) {
			i.setMain_order(o);
			s.save(i);
		}

		userSubscription.setId(o.getAccountIdentifier());
		userSubscription.setUuid(o.getUserId());
		s.save(userSubscription);

		s.getTransaction().commit();
		return o.getAccountIdentifier();
	}

	/**
	 * This function would return all the records Orders or subscriptions for a
	 * user
	 * 
	 * @return Subscription / Order list
	 */
	public List<SubscriptionOrder> fetchUserOrders(String userId) {
		final Session s = sessionFactory.getCurrentSession();

		s.beginTransaction();
		final Query q = s
				.createQuery("SELECT o FROM SubscriptionOrder o WHERE o.userId  = :userId");
		q.setParameter("userId", userId);
		List<SubscriptionOrder> subscriptions = q.list();
		s.getTransaction().commit();
		return subscriptions;
	}

	/**
	 * This function would return all the records Orders or subscriptions
	 * 
	 * @return Subscription / Order list
	 */
	public List<SubscriptionOrder> fetchAllSubscriptions() {
		final Session s = sessionFactory.getCurrentSession();

		s.beginTransaction();
		final Query q = s.createQuery("SELECT r FROM SubscriptionOrder r");
		List<SubscriptionOrder> subscriptions = q.list();
		s.getTransaction().commit();
		return subscriptions;
	}

	public void saveCompany(Company c) {
		final Session s = sessionFactory.getCurrentSession();

		s.beginTransaction();
		s.saveOrUpdate(c);
		s.getTransaction().commit();
	}

	public Company getCompany(String uuid) {
		final Session s = sessionFactory.getCurrentSession();

		s.beginTransaction();
		Company company = (Company) s.get(Company.class, uuid);
		s.getTransaction().commit();
		return company;

	}

	public Item getItem(long id) {
		final Session s = sessionFactory.getCurrentSession();

		s.beginTransaction();
		Item item = (Item) s.get(Item.class, id);
		s.getTransaction().commit();
		return item;

	}

	public List<Item> fetchAllItems() {
		final Session s = sessionFactory.getCurrentSession();

		s.beginTransaction();
		final Query q = s.createQuery("SELECT r FROM Item r");
		List<Item> subscriptions = q.list();
		s.getTransaction().commit();
		return subscriptions;
	}

	public SubscriptionOrder fetchOrder(long id) {
		final Session s = sessionFactory.getCurrentSession();

		s.beginTransaction();
		SubscriptionOrder order = (SubscriptionOrder) s.get(
				SubscriptionOrder.class, id);
		s.getTransaction().commit();
		return order;
	}

	/**
	 * Set the order status to CANCELLED
	 * 
	 * @param payload
	 */
	public void cancelOrder(String id) {
		final Session s = sessionFactory.getCurrentSession();

		s.beginTransaction();

		// Check for active Subscription
		Query q = s
				.createQuery("from  SubscriptionOrder as o where o.accountIdentifier = :accountIdentifier");
		q.setString("accountIdentifier", id);
		List<SubscriptionOrder> result = q.list();

		if (result != null && result.size() > 0) {
			SubscriptionOrder order = result.get(0);
			l.info("order {}", order.getAccountIdentifier());

			order.setStatus(AccStatus.CANCELLED.toString());
			s.save(order);
		}
		s.getTransaction().commit();
	}

	public void assign(EventAssignment event) {
		final UserSubscription userSubscription = new UserSubscription();
		final Session s = sessionFactory.getCurrentSession();
		s.beginTransaction();

		// Check for active Subscription
		Query q = s
				.createQuery("from  SubscriptionOrder as o where o.accountIdentifier = :accountIdentifier");
		q.setString("accountIdentifier", event.getPayload().getAccount()
				.getAccountIdentifier());
		List<SubscriptionOrder> result = q.list();

		if (result != null && result.size() > 0) {
			SubscriptionOrder order = result.get(0);
			l.info("order {}", order.getAccountIdentifier());
			// Check if user is present
			Query userQuery = s
					.createQuery("from  User as u where u.uuid = :uuid");
			userQuery.setString("uuid", event.getPayload().getUser().getUuid());
			List<User> results = userQuery.list();
			final Set<SubscriptionOrder> subOrders;
			final User user;
			if (results != null && results.size() > 0) {
				l.info("existing user");

				// Add subscription for the user
				user = results.get(0);
				// subOrders = user.getSubscriptions();
				// subOrders.add(order);
			} else {
				// create new user and add subscription
				l.info("new user");
				user = event.getPayload().getUser();
				subOrders = new HashSet<SubscriptionOrder>();
				subOrders.add(order);
				// user.setSubscriptions(subOrders);
				l.info("new user {}", user.getAttributes().size());

			}

			for (Entry attr : user.getAttributes()) {
				l.info("key  {}  , {} ", attr.getKey(), attr.getValue());
				if (attr.getKey() != null && attr.getValue() != null) {
					s.save(attr);
				} else {
					user.getAttributes().remove(attr);
				}
			}

			s.save(user);

			userSubscription.setUuid(user.getUuid());
			userSubscription.setId(event.getPayload().getAccount()
					.getAccountIdentifier());

			s.saveOrUpdate(userSubscription);
		}

		s.getTransaction().commit();
	}

	/**
	 * This function would return all the records Users
	 * 
	 * @return users list
	 */
	public List<User> fetchUsers() {
		final Session s = sessionFactory.getCurrentSession();

		s.beginTransaction();
		final Query q = s
				.createSQLQuery("SELECT uuid, firstName , lastName , email , openId , language    FROM APP_User r");
		List<User> users = q.list();
		s.getTransaction().commit();
		return users;
	}

	// Save user subscription
	public void saveUserSubscription(String userUuid,
			String subscriptionAccountIdentifier) {

	}

	public List<UserSubscription> fetchUserSubscription() {
		final Session s = sessionFactory.getCurrentSession();

		s.beginTransaction();
		final Query q = s.createQuery("SELECT r FROM UserSubscription r");
		List<UserSubscription> subscriptions = q.list();
		s.getTransaction().commit();
		return subscriptions;
	}

	public void unassign(EventAssignment event) {
		final Session s = sessionFactory.getCurrentSession();
		s.beginTransaction();

		Query query = s
				.createQuery("delete UserSubscription where id = :id and uuid = :uuid");
		query.setParameter("id", event.getPayload().getAccount()
				.getAccountIdentifier());
		query.setParameter("uuid", event.getPayload().getUser().getUuid());
		int result = query.executeUpdate();
		l.info("id  {} uuid {}  ", event.getPayload().getAccount()
				.getAccountIdentifier(), event.getPayload().getUser().getUuid());
		l.info("sql results {} ", result);

		s.getTransaction().commit();

	}
}
