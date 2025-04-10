package com.flight.management.domain;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.flight.management.proxy.FlightProxy;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class BookingEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String paymentId;

	private String flightId;

	@OneToOne(cascade = CascadeType.ALL)
	private PassengerEntity passenger;

	private Double amount;

	private Integer count;

	@CreationTimestamp
	private Date bookingDate;
}
