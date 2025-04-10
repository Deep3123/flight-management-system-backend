package com.flight.management.service;

import com.flight.management.proxy.BookingProxy;
import com.flight.management.proxy.TicketProxy;

public interface BookingService {
	public void processBooking(BookingProxy bookingProxy);

	public boolean verifyPayment(String paymentId) throws Exception;

	public void generateTicket(TicketProxy ticketProxy);

}
