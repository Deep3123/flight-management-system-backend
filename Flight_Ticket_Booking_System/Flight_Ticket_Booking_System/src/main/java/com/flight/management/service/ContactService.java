package com.flight.management.service;

import java.util.List;

import com.flight.management.proxy.ContactProxy;

public interface ContactService {
	public String saveContactUsDetails(ContactProxy contactProxy);

	public List<ContactProxy> getAllContactUsDetails();

<<<<<<< HEAD:Flight_Ticket_Booking_System/Flight_Ticket_Booking_System/src/main/java/com/flight/management/service/ContactService.java
	public List<ContactProxy> getAllContactUsDetailsByName(String name);

//	public String updateContactUsDetails(ContactProxy contactProxy);

//	public String deleteContactUsDetails(String name);
=======
	public ContactProxy getAllContactUsDetailsByName(String name);

	public String updateContactUsDetails(ContactProxy contactProxy);

	public String deleteContactUsDetails();
>>>>>>> 7b91b131e662fc3e7296914c4ac0bc88a3f9a0e6:Flight_Ticket_Booking_System/src/main/java/com/flight/management/service/ContactService.java
}
