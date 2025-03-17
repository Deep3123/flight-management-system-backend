package com.flight.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flight.management.proxy.ContactProxy;
import com.flight.management.proxy.Response;
import com.flight.management.service.ContactService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/contact")
public class ContactController {

	@Autowired
	private ContactService service;

	@PostMapping("/save-contact-us-details")
	public ResponseEntity<?> saveContactUsDetails(@Valid @RequestBody ContactProxy contactProxy) {
		String s = service.saveContactUsDetails(contactProxy);

		if (s.equals("We got your query, we will reach out you soon!!!"))
			return new ResponseEntity<>(new Response(s, HttpStatus.CREATED.toString()), HttpStatus.CREATED);

		return new ResponseEntity<>(
				new Response("Some error generated while saving data!!!", HttpStatus.PROCESSING.toString()),
				HttpStatus.PROCESSING);
	}

	@GetMapping("/get-all-contact-us-details")
	public ResponseEntity<?> getAllContactUsDetails() {
		List<ContactProxy> list = service.getAllContactUsDetails();

		if (list != null && !list.isEmpty())
			return new ResponseEntity<>(list, HttpStatus.OK);

		return new ResponseEntity<>(new Response("No data to display currently!!!", HttpStatus.NO_CONTENT.toString()),
				HttpStatus.NO_CONTENT);
	}

	@GetMapping("/get-all-contact-us-details-by-name/{name}")
	public ResponseEntity<?> getAllContactUsDetailsByName(@PathVariable("name") String name) {
		List<ContactProxy> list = service.getAllContactUsDetailsByName(name);

		if (list != null && !list.isEmpty())
			return new ResponseEntity<>(list, HttpStatus.OK);

		return new ResponseEntity<>(new Response("No data to display currently!!!", HttpStatus.NO_CONTENT.toString()),
				HttpStatus.NO_CONTENT);
	}
}
