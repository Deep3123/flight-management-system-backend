package com.flight.management.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import com.flight.management.domain.FlightEntity;
import com.flight.management.proxy.FlightProxy;
import com.flight.management.repo.FlightRepo;
import com.flight.management.service.FlightService;
import com.flight.management.util.MapperUtil;

public class FlightServiceImpl implements FlightService {

	@Autowired
	private FlightRepo repo;

	@Override
	public String addFlightDetails(FlightProxy flightProxy) {
		// TODO Auto-generated method stub
		repo.save(MapperUtil.convertValue(flightProxy, FlightEntity.class));
		return "Flight data saved successfully!!";
	}

	@Override
	public List<FlightProxy> getAllFlightsDetails() {
		// TODO Auto-generated method stub
		return MapperUtil.convertListofValue(repo.findAll(), FlightProxy.class);
	}

	@Override
	public FlightProxy getFlightDetailsByFlightNumber(String flightNumber) {
		// TODO Auto-generated method stub
		Optional<FlightEntity> flight = repo.findByFlightNumber(flightNumber);

		if (flight.isPresent())
			return MapperUtil.convertValue(flight.get(), FlightProxy.class);

		return null;
	}

	@Override
	public String updateFlightDetails(FlightProxy flightProxy) {
		// TODO Auto-generated method stub
		Optional<FlightEntity> flight = repo.findByFlightNumber(flightProxy.getFlightNumber());
		
		if(flight.isPresent())
		{
//			if(flightProxy.get)
		}
		return null;
	}

	@Override
	public String deleteFlightDetails(Long flightNumber) {
		// TODO Auto-generated method stub
		return null;
	}

}
