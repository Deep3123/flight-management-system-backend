//package com.flight.management.util;
//
//import java.io.ByteArrayOutputStream;
//import com.flight.management.proxy.PassengerProxy;
//import com.flight.management.proxy.TicketProxy;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.FontFactory;
//import com.itextpdf.text.PageSize;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.pdf.PdfWriter;
//
//public class PDFGenerator {
//	public static byte[] generateTicketPDF(TicketProxy ticketProxy) throws Exception {
//		Document document = new Document(PageSize.A4);
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//		try {
//			// Initialize PDF writer
//			PdfWriter.getInstance(document, out);
//
//			// Open the document
//			document.open();
//
//			// Define fonts
//			Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
//			Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
//
//			for (PassengerProxy p : ticketProxy.getPassengers()) {
//				// Title
//				document.add(new Paragraph("JetWayz Flight Ticket", titleFont));
//				document.add(new Paragraph("\n"));
//
//				// Flight details
//				document.add(new Paragraph("Flight: " + ticketProxy.getFlight().getFlightNumber(), regularFont));
//				document.add(new Paragraph("From: " + ticketProxy.getFlight().getDepartureAirport(), regularFont));
//				document.add(new Paragraph("To: " + ticketProxy.getFlight().getArrivalAirport(), regularFont));
//				document.add(new Paragraph("Departure: " + ticketProxy.getFlight().getDepartureDate() + " "
//						+ ticketProxy.getFlight().getDepartureTime(), regularFont));
//				document.add(new Paragraph("Arrival: " + ticketProxy.getFlight().getArrivalDate() + " "
//						+ ticketProxy.getFlight().getArrivalTime(), regularFont));
//
//				// Passenger details
//				document.add(new Paragraph("\nPassenger Details:", regularFont));
//				document.add(
//						new Paragraph(p.getFirstName() + " " + p.getLastName() + ", Age: " + p.getAge(), regularFont));
//
//				// Add a page break if there are more passengers
//				if (ticketProxy.getPassengers().indexOf(p) < ticketProxy.getPassengers().size() - 1) {
//					document.newPage();
//				}
//			}
//		} catch (DocumentException e) {
//			throw new Exception("Error generating PDF: " + e.getMessage(), e);
//		} finally {
//			// Always close the document
//			if (document != null && document.isOpen()) {
//				document.close();
//			}
//		}
//
//		return out.toByteArray();
//	}
//}

//package com.flight.management.util;
//
//import java.io.ByteArrayOutputStream;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import com.flight.management.proxy.PassengerProxy;
//import com.flight.management.proxy.TicketProxy;
//import com.itextpdf.text.BaseColor;
//import com.itextpdf.text.Chunk;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Element;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.FontFactory;
//import com.itextpdf.text.Image;
//import com.itextpdf.text.PageSize;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.Rectangle;
//import com.itextpdf.text.pdf.Barcode128;
//import com.itextpdf.text.pdf.PdfContentByte;
//import com.itextpdf.text.pdf.PdfPCell;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfWriter;
//import com.itextpdf.text.pdf.draw.DottedLineSeparator;
//import com.itextpdf.text.pdf.draw.LineSeparator;
//
//public class PDFGenerator {
//	// Define reusable colors
//	private static final BaseColor HEADER_COLOR = new BaseColor(0, 102, 204); // Blue for header
//	private static final BaseColor ACCENT_COLOR = new BaseColor(255, 102, 0); // Orange for accent
//	private static final BaseColor LIGHT_GRAY = new BaseColor(240, 240, 240);
//
//	public static byte[] generateTicketPDF(TicketProxy ticketProxy) throws Exception {
//		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//		try {
//			PdfWriter writer = PdfWriter.getInstance(document, out);
//			document.open();
//
//			// Define fonts
//			Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, HEADER_COLOR);
//			Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, HEADER_COLOR);
//			Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.BLACK);
//			Font smallFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.DARK_GRAY);
//			Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.BLACK);
//			Font accentFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, ACCENT_COLOR);
//
//			PdfContentByte cb = writer.getDirectContent();
//
//			for (PassengerProxy passenger : ticketProxy.getPassengers()) {
//				// Create airline logo and header
//				PdfPTable headerTable = new PdfPTable(2);
//				headerTable.setWidthPercentage(100);
//				headerTable.setWidths(new float[] { 1, 3 });
//
//				// Logo cell (you would replace this with your actual logo)
//				PdfPCell logoCell = new PdfPCell();
//				logoCell.setBorder(Rectangle.NO_BORDER);
//				logoCell.setPadding(10);
//
//				// Create a placeholder for the logo
//				Barcode128 code128 = new Barcode128();
//				code128.setCode("JW" + ticketProxy.getFlight().getFlightNumber());
//				code128.setCodeType(Barcode128.CODE128);
//				Image logoImage = code128.createImageWithBarcode(cb, null, null);
//				logoImage.scaleToFit(80, 80);
//				logoCell.addElement(logoImage);
//				headerTable.addCell(logoCell);
//
//				// Title cell
//				PdfPCell titleCell = new PdfPCell();
//				titleCell.setBorder(Rectangle.NO_BORDER);
//				titleCell.setPadding(10);
//				titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//
//				Paragraph title = new Paragraph("JetWayz Airlines", titleFont);
//				Paragraph subtitle = new Paragraph("E-TICKET / BOARDING PASS", subtitleFont);
//				title.setAlignment(Element.ALIGN_RIGHT);
//				subtitle.setAlignment(Element.ALIGN_RIGHT);
//				titleCell.addElement(title);
//				titleCell.addElement(subtitle);
//				headerTable.addCell(titleCell);
//
//				document.add(headerTable);
//
//				// Add a colored separator line
//				LineSeparator ls = new LineSeparator();
//				ls.setLineColor(HEADER_COLOR);
//				ls.setLineWidth(2);
//				document.add(new Chunk(ls));
//
//				document.add(new Paragraph(" ")); // Space
//
//				// Flight info section
//				PdfPTable flightInfoTable = new PdfPTable(3);
//				flightInfoTable.setWidthPercentage(100);
//				flightInfoTable.setWidths(new float[] { 1, 1, 1 });
//
//				// Flight number
//				PdfPCell flightCell = createLabelValueCell("FLIGHT", ticketProxy.getFlight().getFlightNumber(),
//						smallFont, boldFont);
//				flightInfoTable.addCell(flightCell);
//
//				// Date formatting
//				Date departureDate = ticketProxy.getFlight().getDepartureDate();
//				String formattedDepartureDate;
//				try {
//					SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
//					formattedDepartureDate = outputFormat.format(departureDate);
//				} catch (Exception e) {
//					// Fallback in case of formatting error
//					formattedDepartureDate = departureDate.toString();
//				}
//
//				PdfPCell dateCell = createLabelValueCell("DATE", formattedDepartureDate, smallFont, boldFont);
//				flightInfoTable.addCell(dateCell);
//
//				// Boarding time (calculated as 30 min before departure)
//				String departureTime = ticketProxy.getFlight().getDepartureTime();
//				PdfPCell boardingCell = createLabelValueCell("BOARDING TIME", "30 MIN BEFORE DEPARTURE", smallFont,
//						boldFont);
//				flightInfoTable.addCell(boardingCell);
//
//				document.add(flightInfoTable);
//
//				// Route section with large from/to
//				document.add(new Paragraph(" ")); // Space
//
//				PdfPTable routeTable = new PdfPTable(3);
//				routeTable.setWidthPercentage(100);
//				routeTable.setWidths(new float[] { 2, 1, 2 });
//
//				// From
//				PdfPCell fromCell = new PdfPCell();
//				fromCell.setBorder(Rectangle.NO_BORDER);
//				fromCell.setPadding(5);
//
//				Paragraph fromLabel = new Paragraph("FROM", smallFont);
//				Paragraph fromValue = new Paragraph(ticketProxy.getFlight().getDepartureAirport(), accentFont);
//				fromValue.setFont(FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, ACCENT_COLOR));
//
//				fromCell.addElement(fromLabel);
//				fromCell.addElement(fromValue);
//				routeTable.addCell(fromCell);
//
//				// Arrow
//				PdfPCell arrowCell = new PdfPCell(
//						new Paragraph("→", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24)));
//				arrowCell.setBorder(Rectangle.NO_BORDER);
//				arrowCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//				arrowCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//				routeTable.addCell(arrowCell);
//
//				// To
//				PdfPCell toCell = new PdfPCell();
//				toCell.setBorder(Rectangle.NO_BORDER);
//				toCell.setPadding(5);
//
//				Paragraph toLabel = new Paragraph("TO", smallFont);
//				Paragraph toValue = new Paragraph(ticketProxy.getFlight().getArrivalAirport(), accentFont);
//				toValue.setFont(FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, ACCENT_COLOR));
//
//				toCell.addElement(toLabel);
//				toCell.addElement(toValue);
//				routeTable.addCell(toCell);
//
//				document.add(routeTable);
//
//				// Departure and arrival times
//				document.add(new Paragraph(" ")); // Space
//
//				PdfPTable timeTable = new PdfPTable(2);
//				timeTable.setWidthPercentage(100);
//
//				// Departure time
//				PdfPCell depTimeCell = new PdfPCell();
//				depTimeCell.setPadding(10);
//				depTimeCell.setBackgroundColor(LIGHT_GRAY);
//
//				Paragraph depTimeLabel = new Paragraph("DEPARTURE", smallFont);
//				Paragraph depTimeValue = new Paragraph(ticketProxy.getFlight().getDepartureTime(), boldFont);
//
//				depTimeCell.addElement(depTimeLabel);
//				depTimeCell.addElement(depTimeValue);
//				timeTable.addCell(depTimeCell);
//
//				// Arrival time
//				PdfPCell arrTimeCell = new PdfPCell();
//				arrTimeCell.setPadding(10);
//				arrTimeCell.setBackgroundColor(LIGHT_GRAY);
//
//				Paragraph arrTimeLabel = new Paragraph("ARRIVAL", smallFont);
//				Paragraph arrTimeValue = new Paragraph(ticketProxy.getFlight().getArrivalTime(), boldFont);
//
//				arrTimeCell.addElement(arrTimeLabel);
//				arrTimeCell.addElement(arrTimeValue);
//				timeTable.addCell(arrTimeCell);
//
//				document.add(timeTable);
//
//				// Dotted line separator for tear-off
//				document.add(new Paragraph(" ")); // Space
//				DottedLineSeparator dls = new DottedLineSeparator();
//				dls.setGap(3f);
//				dls.setLineWidth(1f);
//				dls.setPercentage(100);
//				document.add(new Chunk(dls));
//				document.add(new Paragraph(" ")); // Space
//
//				// Passenger Information
//				PdfPTable passengerTable = new PdfPTable(2);
//				passengerTable.setWidthPercentage(100);
//
//				// Passenger name
//				PdfPCell nameCell = createLabelValueCell("PASSENGER",
//						passenger.getFirstName() + " " + passenger.getLastName(), smallFont, boldFont);
//				passengerTable.addCell(nameCell);
//
//				// Seat number (simulated for this example)
//				String seatNumber = generateRandomSeat();
//				PdfPCell seatCell = createLabelValueCell("SEAT", seatNumber, smallFont, boldFont);
//				passengerTable.addCell(seatCell);
//
//				document.add(passengerTable);
//
//				// Additional passenger details
//				PdfPTable morePassengerTable = new PdfPTable(3);
//				morePassengerTable.setWidthPercentage(100);
//
//				// Age
//				PdfPCell ageCell = createLabelValueCell("AGE", String.valueOf(passenger.getAge()), smallFont,
//						regularFont);
//				morePassengerTable.addCell(ageCell);
//
//				// Ticket number
//				PdfPCell ticketNumberCell = createLabelValueCell("TICKET NO.", "JW" + generateRandomTicketNumber(),
//						smallFont, regularFont);
//				morePassengerTable.addCell(ticketNumberCell);
//
//				// Class (simulated)
//				PdfPCell classCell = createLabelValueCell("CLASS", "ECONOMY", smallFont, regularFont);
//				morePassengerTable.addCell(classCell);
//
//				document.add(morePassengerTable);
//
//				// Barcode at bottom
//				document.add(new Paragraph(" ")); // Space
//
//				Barcode128 barcode = new Barcode128();
//				barcode.setCode("JW" + ticketProxy.getFlight().getFlightNumber() + generateRandomTicketNumber());
//				barcode.setCodeType(Barcode128.CODE128);
//				Image barcodeImage = barcode.createImageWithBarcode(cb, null, null);
//				barcodeImage.setAlignment(Element.ALIGN_CENTER);
//				barcodeImage.scaleToFit(300, 50);
//				document.add(barcodeImage);
//
//				// Footer with terms
//				document.add(new Paragraph(" ")); // Space
//				Paragraph terms = new Paragraph(
//						"This ticket is non-refundable. Please arrive at the airport at least 2 hours before departure time. "
//								+ "Valid identification is required for all passengers.",
//						smallFont);
//				terms.setAlignment(Element.ALIGN_CENTER);
//				document.add(terms);
//
//				// Add a page break if there are more passengers
//				if (ticketProxy.getPassengers().indexOf(passenger) < ticketProxy.getPassengers().size() - 1) {
//					document.newPage();
//				}
//			}
//
//		} catch (DocumentException e) {
//			throw new Exception("Error generating PDF: " + e.getMessage(), e);
//		} finally {
//			if (document != null && document.isOpen()) {
//				document.close();
//			}
//		}
//
//		return out.toByteArray();
//	}
//
//	// Helper method to create cell with label-value format
//	private static PdfPCell createLabelValueCell(String label, String value, Font labelFont, Font valueFont) {
//		PdfPCell cell = new PdfPCell();
//		cell.setPadding(5);
//		cell.setBorder(Rectangle.NO_BORDER);
//
//		Paragraph labelPara = new Paragraph(label, labelFont);
//		Paragraph valuePara = new Paragraph(value, valueFont);
//
//		cell.addElement(labelPara);
//		cell.addElement(valuePara);
//
//		return cell;
//	}
//
//	// Generate a random seat number for visualization
//	private static String generateRandomSeat() {
//		char[] rows = { 'A', 'B', 'C', 'D', 'E', 'F' };
//		int row = (int) (Math.random() * 30) + 1;
//		char seat = rows[(int) (Math.random() * rows.length)];
//		return row + String.valueOf(seat);
//	}
//
//	// Generate a random ticket number for visualization
//	private static String generateRandomTicketNumber() {
//		StringBuilder sb = new StringBuilder();
//		for (int i = 0; i < 6; i++) {
//			sb.append((int) (Math.random() * 10));
//		}
//		return sb.toString();
//	}
//}

package com.flight.management.util;

import java.io.ByteArrayOutputStream;
import java.awt.Color;
import java.text.SimpleDateFormat;

import com.flight.management.proxy.PassengerProxy;
import com.flight.management.proxy.TicketProxy;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;

public class PDFGenerator {
	// Define reusable colors for a more professional look
	private static final BaseColor PRIMARY_COLOR = new BaseColor(20, 66, 136); // Deep blue
	private static final BaseColor SECONDARY_COLOR = new BaseColor(239, 108, 0); // Vibrant orange
	private static final BaseColor LIGHT_BG = new BaseColor(247, 250, 255); // Very light blue
	private static final BaseColor DARK_TEXT = new BaseColor(32, 32, 32); // Almost black
	private static final BaseColor LIGHT_TEXT = new BaseColor(100, 100, 100); // Medium gray
	private static final BaseColor TICKET_BORDER = new BaseColor(220, 220, 220); // Light gray for borders

	public static byte[] generateTicketPDF(TicketProxy ticketProxy) throws Exception {
		// Use custom page size for ticket-like proportions
		Rectangle ticketSize = new Rectangle(PageSize.A4.getWidth(), PageSize.A4.getWidth() * 0.45f);
		Document document = new Document(ticketSize, 15, 15, 15, 15);
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.open();

			// Define fonts
			Font brandFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, PRIMARY_COLOR);
			Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, PRIMARY_COLOR);
			Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, SECONDARY_COLOR);
			Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 10, DARK_TEXT);
			Font smallFont = FontFactory.getFont(FontFactory.HELVETICA, 8, LIGHT_TEXT);
			Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, DARK_TEXT);
			Font highlightFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, SECONDARY_COLOR);

			PdfContentByte cb = writer.getDirectContent();

			for (PassengerProxy passenger : ticketProxy.getPassengers()) {
				// Main container with rounded corners and border
				PdfPTable mainContainer = new PdfPTable(1);
				mainContainer.setWidthPercentage(100);

				PdfPCell mainCell = new PdfPCell();
				mainCell.setPadding(5);
				mainCell.setBorder(Rectangle.BOX);
				mainCell.setBorderWidth(1.5f);
				mainCell.setBorderColor(TICKET_BORDER);
				mainCell.setBackgroundColor(BaseColor.WHITE);

				// Header with gradient-like effect
				PdfPTable headerTable = new PdfPTable(2);
				headerTable.setWidthPercentage(100);
				headerTable.setWidths(new float[] { 1, 2 });

				// Logo cell
				PdfPCell logoCell = new PdfPCell();
				logoCell.setBorder(Rectangle.NO_BORDER);
				logoCell.setPadding(10);

				// Use a more sophisticated airline logo placeholder
				Paragraph logoText = new Paragraph("JW", brandFont);
				logoText.add(new Chunk("AIRWAYS", FontFactory.getFont(FontFactory.HELVETICA, 14, SECONDARY_COLOR)));
				logoCell.addElement(logoText);

				Paragraph slogan = new Paragraph("FLY WITH CONFIDENCE",
						FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, LIGHT_TEXT));
				logoCell.addElement(slogan);
				headerTable.addCell(logoCell);

				// Title cell
				PdfPCell titleCell = new PdfPCell();
				titleCell.setBorder(Rectangle.NO_BORDER);
				titleCell.setPadding(10);

				Paragraph title = new Paragraph("BOARDING PASS", titleFont);
				Paragraph subtitle = new Paragraph("ELECTRONIC TICKET", smallFont);
				title.setAlignment(Element.ALIGN_RIGHT);
				subtitle.setAlignment(Element.ALIGN_RIGHT);
				titleCell.addElement(title);
				titleCell.addElement(subtitle);

				// Add flight number in the header
				Paragraph flightPara = new Paragraph("FLIGHT: " + ticketProxy.getFlight().getFlightNumber(),
						FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, PRIMARY_COLOR));
				flightPara.setAlignment(Element.ALIGN_RIGHT);
				titleCell.addElement(flightPara);

				headerTable.addCell(titleCell);

				// Add the header to the main container
				PdfPCell headerContainer = new PdfPCell(headerTable);
				headerContainer.setBorder(Rectangle.BOTTOM);
				headerContainer.setBorderColor(PRIMARY_COLOR);
				headerContainer.setBorderWidth(2f);
				headerContainer.setPadding(0);
				mainContainer.addCell(headerContainer);

				// Content container
				PdfPTable contentTable = new PdfPTable(1);
				contentTable.setWidthPercentage(100);

				// Flight route with airports and arrow - main focal point
				PdfPTable routeTable = new PdfPTable(3);
				routeTable.setWidthPercentage(100);
				routeTable.setWidths(new float[] { 2, 1, 2 });
				routeTable.setSpacingBefore(10);

				// From
				PdfPCell fromCell = new PdfPCell();
				fromCell.setBorder(Rectangle.NO_BORDER);
				fromCell.setPadding(5);
				fromCell.setHorizontalAlignment(Element.ALIGN_CENTER);

				Paragraph fromAirport = new Paragraph(ticketProxy.getFlight().getDepartureAirport(), highlightFont);
				fromAirport.setAlignment(Element.ALIGN_CENTER);

				// Add city name (simulated - you would replace with actual city from your data)
				String fromCity = getSimulatedCityName(ticketProxy.getFlight().getDepartureAirport());
				Paragraph fromCityPara = new Paragraph(fromCity, smallFont);
				fromCityPara.setAlignment(Element.ALIGN_CENTER);

				fromCell.addElement(fromAirport);
				fromCell.addElement(fromCityPara);
				routeTable.addCell(fromCell);

				// Arrow with flight duration
				PdfPCell arrowCell = new PdfPCell();
				arrowCell.setBorder(Rectangle.NO_BORDER);
				arrowCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				arrowCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

				Paragraph arrowPara = new Paragraph("✈",
						FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, PRIMARY_COLOR));
				arrowPara.setAlignment(Element.ALIGN_CENTER);

				// Add flight duration (simulated)
				String duration = calculateFlightDuration(ticketProxy.getFlight().getDepartureTime(),
						ticketProxy.getFlight().getArrivalTime());
				Paragraph durationPara = new Paragraph(duration, smallFont);
				durationPara.setAlignment(Element.ALIGN_CENTER);

				arrowCell.addElement(arrowPara);
				arrowCell.addElement(durationPara);
				routeTable.addCell(arrowCell);

				// To
				PdfPCell toCell = new PdfPCell();
				toCell.setBorder(Rectangle.NO_BORDER);
				toCell.setPadding(5);
				toCell.setHorizontalAlignment(Element.ALIGN_CENTER);

				Paragraph toAirport = new Paragraph(ticketProxy.getFlight().getArrivalAirport(), highlightFont);
				toAirport.setAlignment(Element.ALIGN_CENTER);

				// Add city name (simulated - you would replace with actual city from your data)
				String toCity = getSimulatedCityName(ticketProxy.getFlight().getArrivalAirport());
				Paragraph toCityPara = new Paragraph(toCity, smallFont);
				toCityPara.setAlignment(Element.ALIGN_CENTER);

				toCell.addElement(toAirport);
				toCell.addElement(toCityPara);
				routeTable.addCell(toCell);

				// Add route table to content
				PdfPCell routeContainer = new PdfPCell(routeTable);
				routeContainer.setBorder(Rectangle.NO_BORDER);
				routeContainer.setPadding(5);
				routeContainer.setBackgroundColor(LIGHT_BG);
				contentTable.addCell(routeContainer);

				// Flight details section
				PdfPTable flightDetailsTable = new PdfPTable(4);
				flightDetailsTable.setWidthPercentage(100);

				// Format date
				java.util.Date departureDate = ticketProxy.getFlight().getDepartureDate();
				String formattedDepartureDate;
				try {
					SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
					formattedDepartureDate = outputFormat.format(departureDate);
				} catch (Exception e) {
					formattedDepartureDate = departureDate.toString();
				}

				// Date cell
				flightDetailsTable.addCell(createInfoCell("DATE", formattedDepartureDate, smallFont, boldFont));

				// Class cell
				String travelClass = determineTravelClass(passenger);
				flightDetailsTable.addCell(createInfoCell("CLASS", travelClass, smallFont, boldFont));

				// Gate (simulated)
				String gate = generateRandomGate();
				flightDetailsTable.addCell(createInfoCell("GATE", gate, smallFont, boldFont));

				// Boarding time
				String boardingTime = calculateBoardingTime(ticketProxy.getFlight().getDepartureTime());
				flightDetailsTable.addCell(createInfoCell("BOARDING", boardingTime, smallFont, boldFont));

				// Add flight details to content
				PdfPCell flightDetailsContainer = new PdfPCell(flightDetailsTable);
				flightDetailsContainer.setBorder(Rectangle.NO_BORDER);
				flightDetailsContainer.setPadding(5);
				contentTable.addCell(flightDetailsContainer);

				// Departure and arrival times
				PdfPTable timeTable = new PdfPTable(2);
				timeTable.setWidthPercentage(100);

				// Departure time
				PdfPCell depTimeCell = createInfoCell("DEPARTURE", ticketProxy.getFlight().getDepartureTime(),
						smallFont, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, PRIMARY_COLOR));
				depTimeCell.setBackgroundColor(LIGHT_BG);
				timeTable.addCell(depTimeCell);

				// Arrival time
				PdfPCell arrTimeCell = createInfoCell("ARRIVAL", ticketProxy.getFlight().getArrivalTime(), smallFont,
						FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, PRIMARY_COLOR));
				arrTimeCell.setBackgroundColor(LIGHT_BG);
				timeTable.addCell(arrTimeCell);

				// Add times to content
				PdfPCell timeContainer = new PdfPCell(timeTable);
				timeContainer.setBorder(Rectangle.NO_BORDER);
				timeContainer.setPadding(0);
				contentTable.addCell(timeContainer);

				// Passenger information with shaded section
				PdfPTable passengerTable = new PdfPTable(3);
				passengerTable.setWidthPercentage(100);
				passengerTable.setSpacingBefore(10);

				// Passenger name
				PdfPCell passengerCell = createInfoCell("PASSENGER",
						passenger.getFirstName() + " " + passenger.getLastName(), smallFont, boldFont);
				passengerTable.addCell(passengerCell);

				// Seat with accent color
				String seatNumber = generateRandomSeat();
				PdfPCell seatCell = createInfoCell("SEAT", seatNumber, smallFont,
						FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, SECONDARY_COLOR));
				passengerTable.addCell(seatCell);

				// Ticket number
				String ticketNumber = "JW" + generateRandomTicketNumber();
				PdfPCell ticketNumberCell = createInfoCell("TICKET", ticketNumber, smallFont, boldFont);
				passengerTable.addCell(ticketNumberCell);

				// Add passenger info to content
				PdfPCell passengerContainer = new PdfPCell(passengerTable);
				passengerContainer.setBorder(Rectangle.NO_BORDER);
				passengerContainer.setPadding(5);
				passengerContainer.setBackgroundColor(LIGHT_BG);
				contentTable.addCell(passengerContainer);

				// Add the content to the main container
				PdfPCell contentContainer = new PdfPCell(contentTable);
				contentContainer.setBorder(Rectangle.NO_BORDER);
				contentContainer.setPadding(10);
				mainContainer.addCell(contentContainer);

				// Footer with barcode/QR and terms
				PdfPTable footerTable = new PdfPTable(2);
				footerTable.setWidthPercentage(100);
				footerTable.setWidths(new float[] { 1, 2 });

				// QR code
				PdfPCell qrCell = new PdfPCell();
				qrCell.setBorder(Rectangle.NO_BORDER);
				qrCell.setPadding(5);

				// Generate QR code with flight info
				BarcodeQRCode qrCode = new BarcodeQRCode("JETWAYZ:" + ticketProxy.getFlight().getFlightNumber() + ":"
						+ ticketProxy.getFlight().getDepartureAirport() + ":"
						+ ticketProxy.getFlight().getArrivalAirport() + ":" + formattedDepartureDate + ":"
						+ passenger.getLastName(), 90, 90, null);
				Image qrCodeImage = qrCode.getImage();
				qrCodeImage.scaleAbsolute(80, 80);
				qrCell.addElement(qrCodeImage);
				footerTable.addCell(qrCell);

				// Terms and barcode
				PdfPCell termsCell = new PdfPCell();
				termsCell.setBorder(Rectangle.NO_BORDER);
				termsCell.setPadding(5);

				// Add barcode
				Barcode128 barcode = new Barcode128();
				barcode.setCode(ticketNumber);
				barcode.setCodeType(Barcode128.CODE128);
				Image barcodeImage = barcode.createImageWithBarcode(cb, null, null);
				barcodeImage.scaleToFit(180, 30);
				termsCell.addElement(barcodeImage);

				// Add terms
				Paragraph terms = new Paragraph(
						"This electronic ticket is non-transferable and valid only with photo ID. "
								+ "Please arrive at the airport at least 2 hours before departure. "
								+ "Gate closes 20 minutes before departure.",
						smallFont);
				termsCell.addElement(terms);

				footerTable.addCell(termsCell);

				// Add footer to main container
				PdfPCell footerContainer = new PdfPCell(footerTable);
				footerContainer.setBorder(Rectangle.TOP);
				footerContainer.setBorderColor(TICKET_BORDER);
				footerContainer.setPadding(5);
				mainContainer.addCell(footerContainer);

				// Add the main container to the document
				document.add(mainContainer);

				// Add a page break if there are more passengers
				if (ticketProxy.getPassengers().indexOf(passenger) < ticketProxy.getPassengers().size() - 1) {
					document.newPage();
				}
			}

		} catch (DocumentException e) {
			throw new Exception("Error generating PDF: " + e.getMessage(), e);
		} finally {
			if (document != null && document.isOpen()) {
				document.close();
			}
		}

		return out.toByteArray();
	}

	// Helper method to create info cells with consistent styling
	private static PdfPCell createInfoCell(String label, String value, Font labelFont, Font valueFont) {
		PdfPCell cell = new PdfPCell();
		cell.setPadding(5);
		cell.setBorder(Rectangle.NO_BORDER);

		Paragraph labelPara = new Paragraph(label, labelFont);
		Paragraph valuePara = new Paragraph(value, valueFont);

		cell.addElement(labelPara);
		cell.addElement(valuePara);

		return cell;
	}

	// Calculate boarding time (30 min before departure)
	private static String calculateBoardingTime(String departureTime) {
		try {
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
			java.util.Date depTime = timeFormat.parse(departureTime);

			// Subtract 30 minutes
			java.util.Date boardingTime = new java.util.Date(depTime.getTime() - (30 * 60 * 1000));
			return timeFormat.format(boardingTime);
		} catch (Exception e) {
			// If parsing fails, return estimated boarding time
			return "30 MIN BEFORE";
		}
	}

	// Calculate flight duration (simulated)
	private static String calculateFlightDuration(String departureTime, String arrivalTime) {
		try {
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
			java.util.Date depTime = timeFormat.parse(departureTime);
			java.util.Date arrTime = timeFormat.parse(arrivalTime);

			// Calculate duration in minutes
			long durationMillis = arrTime.getTime() - depTime.getTime();
			if (durationMillis < 0) {
				// If arrival is next day, add 24 hours
				durationMillis += 24 * 60 * 60 * 1000;
			}

			long hours = durationMillis / (60 * 60 * 1000);
			long minutes = (durationMillis % (60 * 60 * 1000)) / (60 * 1000);

			return hours + "h " + minutes + "m";
		} catch (Exception e) {
			// Return placeholder if calculation fails
			return "DIRECT";
		}
	}

	// Generate a random seat number
	private static String generateRandomSeat() {
		char[] rows = { 'A', 'B', 'C', 'D', 'E', 'F' };
		int row = (int) (Math.random() * 30) + 1;
		char seat = rows[(int) (Math.random() * rows.length)];
		return row + String.valueOf(seat);
	}

	// Generate a random ticket number
	private static String generateRandomTicketNumber() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 8; i++) {
			sb.append((int) (Math.random() * 10));
		}
		return sb.toString();
	}

	// Generate a random gate number
	private static String generateRandomGate() {
		char terminal = (char) ('A' + (int) (Math.random() * 5));
		int number = (int) (Math.random() * 30) + 1;
		return terminal + String.valueOf(number);
	}

	// Get city name from airport code (simulated)
	private static String getSimulatedCityName(String airportCode) {
		// In a real application, you would look up the city from a database
		// This is just a simulation
		if (airportCode.equals("JFK"))
			return "New York";
		if (airportCode.equals("LAX"))
			return "Los Angeles";
		if (airportCode.equals("LHR"))
			return "London";
		if (airportCode.equals("CDG"))
			return "Paris";
		if (airportCode.equals("DXB"))
			return "Dubai";
		if (airportCode.equals("SIN"))
			return "Singapore";
		if (airportCode.equals("SYD"))
			return "Sydney";
		if (airportCode.equals("HND"))
			return "Tokyo";

		// Return a placeholder for unknown codes
		return airportCode + " Airport";
	}

	// Determine travel class based on passenger data
	private static String determineTravelClass(PassengerProxy passenger) {
		// In a real application, you would get this from ticket data
		// This is just a simulation based on passenger ID or other data
		int randomClass = (int) (Math.random() * 10);
		if (randomClass < 1)
			return "FIRST";
		if (randomClass < 3)
			return "BUSINESS";
		return "ECONOMY";
	}
}