package com.flight.management.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.flight.management.domain.ContactEntity;
import com.flight.management.proxy.ContactProxy;
import com.flight.management.proxy.ContactUs;
import com.flight.management.repo.ContactRepo;
import com.flight.management.service.ContactService;
import com.flight.management.util.MapperUtil;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

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
		// Check if a contact with the same name and message already exists

		try {
			Optional<ContactEntity> details = repo.findByNameAndMessage(contactProxy.getName(),
					contactProxy.getMessage());

			if (details.isEmpty()) {
				// Create the HTML content for the email

//				SimpleMailMessage mailMessage = new SimpleMailMessage();
//				mailMessage.setFrom(sender); // Assuming sender is a variable containing the email address of the sender
//				mailMessage.setTo(contactProxy.getEmail());
//				mailMessage.setSubject("We Received Your Query");
//				mailMessage.setText("Dear " + contactProxy.getName() + ",\n\n"
//						+ "Thank you for reaching out to us regarding the JetWayz."
//						+ "We have received your query and our team is currently reviewing it. "
//						+ "We appreciate your patience and will get back to you as soon as possible.\n\n"
//						+ "If you need any further assistance in the meantime, feel free to contact our support team.\n\n"
//						+ "Best regards,\n" + "The Support Team");
//
//				// Send the email
//				javaMailSender.send(mailMessage);

				String htmlContent = "<div style=\"font-family: Arial, sans-serif; padding: 20px; background-color: #f5f5f5;\">"
						+ "<h2 style=\"color: #333;\">Thank you for contacting JetWayz</h2>" + "<p>Dear "
						+ contactProxy.getName() + ",</p>"
						+ "<p>Thank you for reaching out to us regarding JetWayz. We have received your query, and our team is currently reviewing it.</p>"
						+ "<p>We appreciate your patience and will get back to you as soon as possible.</p>"
						+ "<p>If you need any further assistance, feel free to contact our support team at any time.</p>"
						+ "<br>" + "<p>Best regards,<br><strong>The Support Team</strong><br>JetWayz</p>" + "</div>";

				// Create the MimeMessage for sending HTML email
				MimeMessage mimeMessage = javaMailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

				// Set the email details
				helper.setFrom(sender); // Sender email address
				helper.setTo(contactProxy.getEmail()); // Recipient's email address
				helper.setSubject("We Received Your Query");
				helper.setText(htmlContent, true); // Mark the content as HTML

				// Send the email
				javaMailSender.send(mimeMessage);

				// Save the contact details to the repository
				repo.save(MapperUtil.convertValue(contactProxy, ContactEntity.class));

				// Return a success message
				return "Thank you for reaching out! We have received your query and will get back to you soon. "
						+ "A confirmation email has been sent to your registered email address.";
			}
		} catch (MessagingException e) {
			// TODO: handle exception
			return "Error generated while sending email.";
		}
		// Return a message if the query already exists in the system
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
	public String deleteContactUsDetails(ContactUs contact) {
		// TODO Auto-generated method stub
		Optional<ContactEntity> detail = repo.findByNameAndMessage(contact.getName(), contact.getMessage());

		if (detail.isPresent()) {
			repo.delete(detail.get());
			return "Data successfully deleted.";
		}
		return "Data not found with given name and message.";
	}
}
