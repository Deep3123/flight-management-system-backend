package com.flight.management.service.impl;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import com.flight.management.proxy.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
import com.flight.management.repo.PassengerRepo;
import com.flight.management.service.BookingService;
import com.flight.management.util.MapperUtil;
import com.flight.management.util.PDFGenerator;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;

import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;

@Service
@Slf4j
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

	@Autowired
	private PassengerRepo passengerRepo;

	@Value("${spring.mail.username}")
	private String sender;

    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

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

//			Optional<FlightEntity> flight = repo.findByFlightNumber(request.getFlightId());
//
//			if (flight.isPresent()) {
//				flight.get().setSeatsAvailable(Math.abs(flight.get().getSeatsAvailable() - request.getCount()));
//				repo.save(flight.get());
//			}
//
//			bookingRepo.save(MapperUtil.convertValue(request, BookingEntity.class));

			// 2. Save Passenger separately
			PassengerEntity passenger = new PassengerEntity();
			passenger.setFirstName(request.getPassenger().getFirstName());
			passenger.setLastName(request.getPassenger().getLastName());
			passenger.setAge(request.getPassenger().getAge());
			passenger.setEmail(request.getPassenger().getEmail());
			passenger.setMobile(request.getPassenger().getMobile());
			passenger.setCountryCode(request.getPassenger().getCountryCode());
			passenger.setCreatedAt(new Date());

			PassengerEntity savedPassenger = passengerRepo.save(passenger); // save first

			// 3. Update available seats in flight
			Optional<FlightEntity> flight = repo.findByFlightNumber(request.getFlightId());
			if (flight.isPresent()) {
				flight.get().setSeatsAvailable(Math.abs(flight.get().getSeatsAvailable() - request.getCount()));
				repo.save(flight.get());
			}

			// 4. Save Booking and link the saved passenger
			BookingEntity booking = new BookingEntity();
			booking.setPaymentId(request.getPaymentId());
			booking.setAmount(request.getAmount());
			booking.setFlightId(request.getFlightId());
			booking.setCount(request.getCount());
			booking.setBookingDate(new Date());
			booking.setPassenger(savedPassenger); // link DBRef here

			bookingRepo.save(booking); // now booking has a valid DBRef to the passenger
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
//		System.err.println(bookingRepo.findAll());
		List<BookingEntity> bookings = bookingRepo.findAll();
		return bookings.stream().map(b -> {
			BookingDetails dto = new BookingDetails();
//			System.err.println(b.getId());
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
			passengerRepo.delete(booking.get().getPassenger());
			bookingRepo.delete(booking.get());
			return "Booking details deleted successfully.";
		}
		return null;
	}

    @Override
    public Response downloadAllBookingData() {
        try {
            log.info("Entry into downloadAllBookingData()");

            List<BookingEntity> bookings = bookingRepo.findAll();
            SXSSFWorkbook workbook = new SXSSFWorkbook(100);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Sheet sheet = workbook.createSheet("Bookings");
            Row headerRow = sheet.createRow(0);

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);

            String[] headers = {"Sr. No.", "Booking ID", "Payment ID", "Flight ID", "Passenger Name", "Passenger Email", "Passenger Mobile", "Amount", "Seat Count", "Booking Date"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (BookingEntity booking : bookings) {
                Row row = sheet.createRow(rowNum);
                row.createCell(0).setCellValue(rowNum);
                row.createCell(1).setCellValue(booking.getId());
                row.createCell(2).setCellValue(booking.getPaymentId());
                row.createCell(3).setCellValue(booking.getFlightId());
                row.createCell(4).setCellValue(booking.getPassenger().getFirstName() + " " + booking.getPassenger().getLastName());
                row.createCell(5).setCellValue(booking.getPassenger().getEmail());
                row.createCell(6).setCellValue(booking.getPassenger().getCountryCode() + " " + booking.getPassenger().getMobile());
                row.createCell(7).setCellValue(currencyFormatter.format(booking.getAmount()));
                row.createCell(8).setCellValue(booking.getCount());
                row.createCell(9).setCellValue(booking.getBookingDate().toString());
                rowNum++;
            }

            workbook.write(outputStream);
            workbook.close();

            log.info("Excel file downloaded successfully for all booking data !!");

            return Response.builder().data(outputStream.toByteArray()).message("All booking data downloaded successfully !")
                    .status_code(HttpStatus.OK.toString()).build();
        } catch (Exception e) {
            log.error("Error generating excel: {}", e.getMessage(), e);
            return Response.builder().message("Error generating excel file")
                    .status_code(HttpStatus.INTERNAL_SERVER_ERROR.toString()).build();
        }
    }
}
