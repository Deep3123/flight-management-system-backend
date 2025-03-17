package com.flight.management.service.impl;

import java.util.List;

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

}
