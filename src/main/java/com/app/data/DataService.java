package com.app.data;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.AppCfg;
import com.app.model.SubscriptionOrder;
import com.app.model.User;
import com.app.model.UserSubscription;

@Controller
@RequestMapping("/dataservice")
@Singleton
public class DataService {
	private final Logger l = LoggerFactory.getLogger(DataService.class);
	private DataStoreImpl dataStoreImpl;
	private AppCfg cfg;

	@Inject
	public DataService(final AppCfg cfg, final DataStoreImpl dataStoreImpl) {
		this.cfg = cfg;
		this.dataStoreImpl = dataStoreImpl;
	}

	@RequestMapping(value = "/subscriptions", produces = "application/json")
	@ResponseBody
	public String fetchOrders() {
		List<SubscriptionOrder> subscriptions = dataStoreImpl
				.fetchAllSubscriptions();

		if (subscriptions == null) {
			return "";
		}

		try {
			return cfg.getObjectMapper().writeValueAsString(subscriptions);
		} catch (IOException e) {

			l.error("error {}", e);
		}

		return "";
	}

	@RequestMapping(value = "/users", produces = "application/json")
	@ResponseBody
	public String fetchUsers() {
		List<User> users = dataStoreImpl.fetchUsers();

		if (users == null) {
			return "";
		}

		try {
			return cfg.getObjectMapper().writeValueAsString(users);
		} catch (IOException e) {

			l.error("error {}", e);
		}

		return "";
	}

	@RequestMapping(value = "/usersubs", produces = "application/json")
	@ResponseBody
	public String fetchUserSubscription() {
		List<UserSubscription> usersub = dataStoreImpl.fetchUserSubscription();

		if (usersub == null) {
			return "";
		}

		try {
			return cfg.getObjectMapper().writeValueAsString(usersub);
		} catch (IOException e) {

			l.error("error {}", e);
		}

		return "";
	}
}
