package com.flight.management.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.flight.management.domain.BookingEntity;
import com.flight.management.domain.FlightEntity;
import com.flight.management.domain.PassengerEntity;
import com.flight.management.proxy.BookingDetails;
import com.flight.management.proxy.BookingProxy;
import com.flight.management.proxy.TicketProxy;
import com.flight.management.repo.BookingRepo;
import com.flight.management.repo.FlightRepo;
import com.flight.management.service.BookingService;
import com.flight.management.util.MapperUtil;
import com.flight.management.util.PDFGenerator;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
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

	@Autowired
	private FlightRepo repo;

	@Value("${spring.mail.username}")
	private String sender;

	@Override
	public boolean verifyPayment(String paymentId) throws Exception {
		RazorpayClient razorpay = new RazorpayClient(razorpayKey, razorpaySecret);
		Payment payment = razorpay.payments.fetch(paymentId);
		return "authorized".equals(payment.get("status"));
	}

	@Override
	public void processBooking(BookingProxy request) {
		// TODO Auto-generated method stub
		try {
			// 1. Verify payment with Razorpay (optional step if you use webhook too)
//			RazorpayClient razorpay = new RazorpayClient(razorpayKey, razorpaySecret);
//			Payment payment = razorpay.payments.fetch(request.getPaymentId());
////			System.out.println("Payment status: " + payment.get("status"));
//
////			System.err.println(request);
//
//			if (!"authorized".equals(payment.get("status"))) {
//				throw new Exception("Payment not successful");
//			}

			// 2. Save to DB
//			BookingEntity booking = new BookingEntity();
//			booking.setPaymentId(request.getPaymentId());
//			booking.setAmount(request.getAmount());
//			booking.setPrimaryPassengerEmail(request.getPassenger().get(0).getEmail());
//			booking.setPassenger(new ObjectMapper().writeValueAsString(request.getPassenger()));
//			booking.setFlightJson(new ObjectMapper().writeValueAsString(request.getFlight()));
//			bookingRepo.save(booking);

			Optional<FlightEntity> flight = repo.findByFlightNumber(request.getFlightId());

			if (flight.isPresent()) {
				flight.get().setSeatsAvailable(Math.abs(flight.get().getSeatsAvailable() - request.getCount()));
				repo.save(flight.get());
			}

			bookingRepo.save(MapperUtil.convertValue(request, BookingEntity.class));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace(); // Add this line
			throw new RuntimeException("Razorpay Connection may not be established!!!");
		}

	}

//	@Override
//	public void generateTicket(TicketProxy ticketProxy) {
//		// TODO Auto-generated method stub
//		try {
//			// 3. Generate PDF
//			byte[] pdfBytes = PDFGenerator.generateTicketPDF(ticketProxy);
//
//			// 4. Send Email
////			        EmailUtil.sendTicketEmail(mailSender, request.getPassengerList().get(0).getEmail(), pdfBytes);
//			MimeMessage message = mailSender.createMimeMessage();
//			MimeMessageHelper helper = new MimeMessageHelper(message, true);
//			helper.setFrom(sender);
//			helper.setTo(ticketProxy.getPassengers().get(0).getEmail());
//			helper.setSubject("Your JetWayz Flight Ticket");
//			helper.setText(
//					"Thank you for choosing JetWayz! Your flight ticket is attached to this email. We wish you a pleasant journey.",
//					true);
//
//			ByteArrayDataSource dataSource = new ByteArrayDataSource(pdfBytes, "application/pdf");
//			helper.addAttachment("ticket.pdf", dataSource);
//			mailSender.send(message);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	}

	@Override
	public void generateTicket(TicketProxy ticketProxy) {
		try {
			// 3. Generate PDF ticket
			byte[] pdfBytes = PDFGenerator.generateTicketPDF(ticketProxy);

			// 4. Create the email with HTML content
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			// Set email properties
			helper.setFrom(sender); // Sender email address
			helper.setTo(ticketProxy.getPassengers().get(0).getEmail()); // Recipient's email address
			helper.setSubject("Your JetWayz Flight Ticket");

			// HTML email content with inline styles
			String htmlContent = "<div style=\"font-family: Arial, sans-serif; padding: 20px; background-color: #f5f5f5;\">"
					+ "<h2 style=\"color: #333;\">Thank you for choosing JetWayz!</h2>"
					+ "<p style=\"color: #555;\">We are pleased to confirm your flight reservation. Your flight ticket is attached to this email. We wish you a pleasant journey.</p>"
					+ "<p style=\"color: #555;\">If you have any questions, feel free to contact our support team.</p>"
					+ "<br>" + "<p style=\"color: #333;\">Best regards,<br><strong>The JetWayz Team</strong></p>"
					+ "</div>";

			// Set the email body and mark it as HTML
			helper.setText(htmlContent, true);

			// Attach the PDF ticket
			ByteArrayDataSource dataSource = new ByteArrayDataSource(pdfBytes, "application/pdf");
			helper.addAttachment("ticket.pdf", dataSource);

			// Send the email
			mailSender.send(message);

		} catch (Exception e) {
			// Handle exceptions (e.g., log them, rethrow, or return an error response)
			e.printStackTrace();

		}
	}

	@Override
	public List<BookingDetails> getAllMergedBookings() {
		// TODO Auto-generated method stub
		List<BookingEntity> bookings = bookingRepo.findAll();
		return bookings.stream().map(b -> {
			BookingDetails dto = new BookingDetails();
			dto.setId(b.getId());
			dto.setPaymentId(b.getPaymentId());
			dto.setFlightId(b.getFlightId());
			dto.setAmount(b.getAmount());
			dto.setCount(b.getCount());
			dto.setBookingDate(b.getBookingDate());

			PassengerEntity p = b.getPassenger();
			dto.setFirstName(p.getFirstName());
			dto.setLastName(p.getLastName());
			dto.setAge(p.getAge());
			dto.setEmail(p.getEmail());
			dto.setCountryCode(p.getCountryCode());
			dto.setMobile(p.getMobile());

			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public String deleteBookingDetails(String paymentId) {
		// TODO Auto-generated method stub
		Optional<BookingEntity> booking = bookingRepo.findByPaymentId(paymentId);

		if (booking.isPresent()) {
			bookingRepo.delete(booking.get());
			return "Booking details deleted successfully.";
		}
		return null;
	}
}
