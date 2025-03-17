package com.flight.management.service.impl;

import java.util.List;
<<<<<<< HEAD:Flight_Ticket_Booking_System/Flight_Ticket_Booking_System/src/main/java/com/flight/management/service/impl/ContactServiceImpl.java
import java.util.Optional;
=======
>>>>>>> 7b91b131e662fc3e7296914c4ac0bc88a3f9a0e6:Flight_Ticket_Booking_System/src/main/java/com/flight/management/service/impl/ContactServiceImpl.java

import org.springframework.beans.factory.annotation.Autowired;

import com.flight.management.domain.ContactEntity;
import com.flight.management.proxy.ContactProxy;
import com.flight.management.repo.ContactRepo;
import com.flight.management.service.ContactService;
import com.flight.management.util.MapperUtil;

public class ContactServiceImpl implements ContactService {

	@Autowired
	private ContactRepo repo;

	@Override
	public String saveContactUsDetails(ContactProxy contactProxy) {
		// TODO Auto-generated method stub
		repo.save(MapperUtil.convertValue(contactProxy, ContactEntity.class));
		return "We got your query, we will reach out you soon!!!";
	}

	@Override
	public List<ContactProxy> getAllContactUsDetails() {
		// TODO Auto-generated method stub
		return MapperUtil.convertListofValue(repo.findAll(), ContactProxy.class);
	}

	@Override
<<<<<<< HEAD:Flight_Ticket_Booking_System/Flight_Ticket_Booking_System/src/main/java/com/flight/management/service/impl/ContactServiceImpl.java
	public List<ContactProxy> getAllContactUsDetailsByName(String name) {
		// TODO Auto-generated method stub
		Optional<List<ContactEntity>> list = repo.findByName(name);

		if (list.isPresent())
			return MapperUtil.convertListofValue(list.get(), ContactProxy.class);

		return null;
	}

//	@Override
//	public String updateContactUsDetails(ContactProxy contactProxy) {
//		// TODO Auto-generated method stub
//		Optional<List<ContactEntity>> list = repo.findByName(contactProxy.getName());
//		
//		if(list.isPresent())
//		{
//			
//		}
//		return null;
//	}

//	@Override
//	public String deleteContactUsDetails() {
//		// TODO Auto-generated method stub
//		return null;
//	}
=======
	public ContactProxy getAllContactUsDetailsByName(String name) {
		// TODO Auto-generated method stub
		return MapperUtil.convertValue(repo.findByName(name).get(), ContactProxy.class);
	}

	@Override
	public String updateContactUsDetails(ContactProxy contactProxy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deleteContactUsDetails() {
		// TODO Auto-generated method stub
		return null;
	}
>>>>>>> 7b91b131e662fc3e7296914c4ac0bc88a3f9a0e6:Flight_Ticket_Booking_System/src/main/java/com/flight/management/service/impl/ContactServiceImpl.java

}
