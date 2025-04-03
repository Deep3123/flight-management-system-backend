package com.flight.management.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flight.management.proxy.ContactProxy;
import com.flight.management.proxy.ContactUs;
import com.flight.management.proxy.Response;
import com.flight.management.service.ContactService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/contact")
public class ContactController {

	@Autowired
	private ContactService service;

	@PostMapping("/save-contact-us-details")
	public ResponseEntity<?> saveContactUsDetails(@Valid @RequestBody ContactProxy contactProxy,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			// Extract error messages
			List<String> errors = bindingResult.getFieldErrors().stream().map(error -> error.getDefaultMessage())
					.collect(Collectors.toList());

			return new ResponseEntity<>(new Response(errors.toString(), HttpStatus.BAD_REQUEST.toString()),
					HttpStatus.BAD_REQUEST);
		}

		String s = service.saveContactUsDetails(contactProxy);

		if (s.equals("Thank you for reaching out! We have received your query and will get back to you soon. "
				+ "A confirmation email has been sent to your registered email address."))
			return new ResponseEntity<>(new Response(s, HttpStatus.CREATED.toString()), HttpStatus.CREATED);

		return new ResponseEntity<>(new Response(s, HttpStatus.PROCESSING.toString()), HttpStatus.PROCESSING);
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

	@PostMapping("/delete-contact-us-details")
	public ResponseEntity<?> deleteContactUsDetails(@Valid @RequestBody ContactUs contact) {
		// TODO Auto-generated method stub
		String s = service.deleteContactUsDetails(contact);

		if (s != null && !s.isEmpty() && s == "Data successfully deleted.")
			return new ResponseEntity<>(new Response(s, HttpStatus.OK.toString()), HttpStatus.OK);

		return new ResponseEntity<>(
				new Response("Data not deleted, May be some error generated!!!", HttpStatus.NO_CONTENT.toString()),
				HttpStatus.NO_CONTENT);
	}
}