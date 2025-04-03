package com.flight.management.service;

import java.util.List;

import com.flight.management.proxy.ContactProxy;
import com.flight.management.proxy.ContactUs;

public interface ContactService {
	public String saveContactUsDetails(ContactProxy contactProxy);

	public List<ContactProxy> getAllContactUsDetails();

	public List<ContactProxy> getAllContactUsDetailsByName(String name);

//	public String updateContactUsDetails(ContactProxy contactProxy);

	public String deleteContactUsDetails(ContactUs contact);
}
