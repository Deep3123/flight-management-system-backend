package com.flight.management.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.flight.management.domain.BookingEntity;
import com.flight.management.proxy.BookingProxy;
import com.flight.management.repo.BookingRepo;
import com.flight.management.service.BookingService;
import com.flight.management.util.MapperUtil;
import com.flight.management.util.PDFGenerator;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;

import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;

@Service
public class BookingServiceImpl implements BookingService {
	@Value("${rzp_key_id}")
	private String razorpayKey;

	@Value("${rzp_key_secret}")
	private String razorpaySecret;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private BookingRepo bookingRepo;

	@Value("${spring.mail.username}")
	private String sender;

	@Override
	public void processBooking(BookingProxy request) {
		// TODO Auto-generated method stub
		try {
			// 1. Verify payment with Razorpay (optional step if you use webhook too)
			RazorpayClient razorpay = new RazorpayClient(razorpayKey, razorpaySecret);
			Payment payment = razorpay.payments.fetch(request.getPaymentId());

			if (!"captured".equals(payment.get("status"))) {
				throw new Exception("Payment not successful");
			}

			// 2. Save to DB
//			BookingEntity booking = new BookingEntity();
//			booking.setPaymentId(request.getPaymentId());
//			booking.setAmount(request.getAmount());
//			booking.setPrimaryPassengerEmail(request.getPassenger().get(0).getEmail());
//			booking.setPassenger(new ObjectMapper().writeValueAsString(request.getPassenger()));
//			booking.setFlightJson(new ObjectMapper().writeValueAsString(request.getFlight()));
//			bookingRepo.save(booking);

			bookingRepo.save(MapperUtil.convertValue(request, BookingEntity.class));

			// 3. Generate PDF
			byte[] pdfBytes = PDFGenerator.generateTicketPDF(request);

			// 4. Send Email
//				        EmailUtil.sendTicketEmail(mailSender, request.getPassengerList().get(0).getEmail(), pdfBytes);
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(sender);
			helper.setTo(request.getPassenger().getEmail());
			helper.setSubject("Your JetWayz Flight Ticket");
			helper.setText("Thank you for booking with JetWayz. Find your ticket attached.", true);

			ByteArrayDataSource dataSource = new ByteArrayDataSource(pdfBytes, "application/pdf");
			helper.addAttachment("ticket.pdf", dataSource);

			mailSender.send(message);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}
