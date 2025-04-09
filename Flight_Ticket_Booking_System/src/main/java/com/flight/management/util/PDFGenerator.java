package com.flight.management.util;

import com.flight.management.proxy.BookingProxy;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

public class PDFGenerator {
	public static byte[] generateTicketPDF(BookingProxy request) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PdfWriter writer = new PdfWriter(out);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf);

		document.add(new Paragraph("JetWayz Flight Ticket").setBold().setFontSize(18));
		document.add(new Paragraph("Flight: " + request.getFlightId().getFlightNumber()));
		document.add(new Paragraph("From: " + request.getFlightId().getDepartureAirport()));
		document.add(new Paragraph("To: " + request.getFlightId().getArrivalAirport()));
		document.add(new Paragraph("Departure: " + request.getFlightId().getDepartureDate() + " "
				+ request.getFlightId().getDepartureTime()));
		document.add(new Paragraph(
				"Arrival: " + request.getFlightId().getArrivalDate() + " " + request.getFlightId().getArrivalTime()));

		document.add(new Paragraph("\nPassenger Details:"));

//		document.add(new Paragraph(request.getFlightId().getFirstName() + " " + p.getLastName() + ", Age: " + p.getAge()));

		document.close();
		return out.toByteArray();
	}
}
