package com.app.controller;

import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

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
import com.app.model.EventAssignment;

@Controller
@RequestMapping("/rest/api/events/")
public class AssignmentController {
	private Logger l = LoggerFactory.getLogger(AssignmentController.class);

	@Autowired
	private AppCfg service;
	@Autowired
	private DataStoreImpl dataStoreImpl;

	@RequestMapping(value = "/assign", produces = "application/xml")
	@ResponseBody
	public IntegrationAppResponse assignEvent(@RequestParam String token,
			@RequestParam String url) {
		String identifier = "";

		try {
			l.info("assign  eventUrl: " + url);
			HttpResponse resp = service.eventData(url);

			JAXBContext context = JAXBContext
					.newInstance(EventAssignment.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			EventAssignment event = (EventAssignment) unmarshaller
					.unmarshal(resp.getEntity().getContent());

			identifier = event.getPayload().getAccount().getAccountIdentifier();

			l.info("{}", event);
			// Assignment
			dataStoreImpl.assign(event);

		} catch (IOException | JAXBException e) {
			l.error("Error {}", e);
			return new IntegrationAppResponse(false, "UNKNOWN_ERROR",
					e.getMessage(), "");
		}

		return new IntegrationAppResponse(true, "", "", identifier);
	}

	@RequestMapping(value = "/unassign", produces = "application/xml")
	@ResponseBody
	public IntegrationAppResponse unassignEvent(@RequestParam String token,
			@RequestParam String url) {
		String identifier = "";

		try {
			l.info("assign  eventUrl: " + url);
			HttpResponse resp = service.eventData(url);

			JAXBContext context = JAXBContext
					.newInstance(EventAssignment.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			EventAssignment event = (EventAssignment) unmarshaller
					.unmarshal(resp.getEntity().getContent());

			identifier = event.getPayload().getAccount().getAccountIdentifier();

			l.info("{}", event);
			// UnAssignment
			dataStoreImpl.unassign(event);

		} catch (IOException | JAXBException e) {
			l.error("Error {}", e);
			return new IntegrationAppResponse(false, "UNKNOWN_ERROR",
					e.getMessage(), "");
		}

		return new IntegrationAppResponse(true, "", "", identifier);
	}
}
