package com.app.controller;

import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.AppCfg;
import com.app.data.DataStoreImpl;
import com.app.model.Event;

import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

@Controller
@RequestMapping("/api/integration/v1/events")
public class SubscriptionOrderController {
	private Logger l = LoggerFactory
			.getLogger(SubscriptionOrderController.class);

	@Autowired
	private AppCfg service;
	@Autowired
	private DataStoreImpl dataStoreImpl;

	@Autowired
	public SubscriptionOrderController(AppCfg service,
			DataStoreImpl dataStoreImpl) {
		this.dataStoreImpl = dataStoreImpl;
	}

	@RequestMapping(value = "/create", produces = "application/xml")
	@ResponseBody
	public IntegrationAppResponse createOrderEvent(@RequestParam String token,
			@RequestParam String url) {
		String identifier;

		try {
			l.info("create :  eventUrl: " + url);
			HttpResponse resp = service.eventData(url);

			JAXBContext context = JAXBContext.newInstance(Event.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			Event event = (Event) unmarshaller.unmarshal(resp.getEntity()
					.getContent());

			// Save order
			identifier = dataStoreImpl.saveOrder(event);
		} catch (IOException | JAXBException e) {

			l.error("Error {}", e);
			return new IntegrationAppResponse(false, "UNKNOWN_ERROR",
					e.getMessage(), "");
		}

		// return response
		return new IntegrationAppResponse(true, "", "", identifier);
	}

	@RequestMapping(value = "/cancel", produces = "application/xml")
	@ResponseBody
	public IntegrationAppResponse cancelEvent(@RequestParam String token,
			@RequestParam String url) {
		String identifier = "";

		try {
			l.info("cancel  eventUrl: " + url);
			HttpResponse resp = service.eventData(url);

			JAXBContext context = JAXBContext.newInstance(Event.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			Event event = (Event) unmarshaller.unmarshal(resp.getEntity()
					.getContent());

			identifier = event.getPayload().getAccount().getAccountIdentifier();

			// Cancel order
			dataStoreImpl.cancelOrder(identifier);

		} catch (IOException | JAXBException e) {
			l.error("Error {}", e);
			return new IntegrationAppResponse(false, "UNKNOWN_ERROR",
					e.getMessage(), "");
		}

		return new IntegrationAppResponse(true, "", "", identifier);
	}
}
