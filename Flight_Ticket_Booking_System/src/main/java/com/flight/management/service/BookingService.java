package com.flight.management.service;

import java.util.List;

import com.flight.management.proxy.BookingDetails;
import com.flight.management.proxy.BookingProxy;
import com.flight.management.proxy.Response;
import com.flight.management.proxy.TicketProxy;

public interface BookingService {
	public void processBooking(BookingProxy bookingProxy);

	public boolean verifyPayment(String paymentId) throws Exception;

	public void generateTicket(TicketProxy ticketProxy);

	public List<BookingDetails> getAllMergedBookings();

	public String deleteBookingDetails(String paymentId);

    public Response downloadAllBookingData();

}
