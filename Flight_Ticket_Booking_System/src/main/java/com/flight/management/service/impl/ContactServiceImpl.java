package com.flight.management.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.flight.management.domain.ContactEntity;
import com.flight.management.proxy.ContactProxy;
import com.flight.management.repo.ContactRepo;
import com.flight.management.service.ContactService;
import com.flight.management.util.MapperUtil;

@Service
public class ContactServiceImpl implements ContactService {

	@Autowired
	private ContactRepo repo;

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String sender;

	@Override
	public String saveContactUsDetails(ContactProxy contactProxy) {
		// TODO Auto-generated method stub
		Optional<ContactEntity> details = repo.findByNameAndMessage(contactProxy.getName(), contactProxy.getMessage());

		if (details.isEmpty()) {
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setFrom(sender); // Assuming `sender` is a variable containing the email address of the sender
			mailMessage.setTo(contactProxy.getEmail());
			mailMessage.setSubject("We Received Your Query");
			mailMessage.setText("Dear " + contactProxy.getName() + ",\n\n"
					+ "Thank you for reaching out to us regarding the Flight Ticket Booking System. "
					+ "We have received your query and our team is currently reviewing it. "
					+ "We appreciate your patience and will get back to you as soon as possible.\n\n"
					+ "If you need any further assistance in the meantime, feel free to contact our support team.\n\n"
					+ "Best regards,\n" + "The Support Team");

			// Send the email
			javaMailSender.send(mailMessage);

			// Save the contact details to the repository
			repo.save(MapperUtil.convertValue(contactProxy, ContactEntity.class));	

			// Return success message
			return "Thank you for reaching out! We have received your query and will get back to you soon. "
					+ "A confirmation email has been sent to your registered email address.";
		}

		return "We have already received your query. Our team is reviewing it, and we will respond to you shortly. "
				+ "Thank you for your patience!";
	}

	@Override
	public List<ContactProxy> getAllContactUsDetails() {
		// TODO Auto-generated method stub
		return MapperUtil.convertListofValue(repo.findAll(), ContactProxy.class);
	}

	@Override
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

	@Override
	public String deleteContactUsDetails(String name, String message) {
		// TODO Auto-generated method stub
		return null;
	}
}
