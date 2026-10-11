package com.flight.management.service.impl;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.flight.management.proxy.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.flight.management.domain.FlightEntity;
import com.flight.management.proxy.AirportRouteProjection;
import com.flight.management.proxy.FlightProxy;
import com.flight.management.proxy.FlightSearchProxy;
import com.flight.management.repo.FlightRepo;
import com.flight.management.service.FlightService;
import com.flight.management.util.MapperUtil;

@Service
@Slf4j
public class FlightServiceImpl implements FlightService {

	@Autowired
	private FlightRepo repo;

    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

//	@Override
//	public String addFlightDetails(FlightProxy flightProxy) {
//		// TODO Auto-generated method stub
//		Optional<FlightEntity> flight = repo.findByFlightNumber(flightProxy.getFlightNumber());
//
//		if (flight.isEmpty() || flight.get() == null) {
//			flight.get().setCreatedAt(new Date());
//			flight.get().setUpdatedAt(new Date());
//
//			repo.save(MapperUtil.convertValue(flightProxy, FlightEntity.class));
//			return "Flight data saved successfully!!";
//		}
//
//		return "Flight already exist with given flight number.";
//	}

	@Override
	public String addFlightDetails(FlightProxy flightProxy) {
		// Fetch the flight based on flight number
		Optional<FlightEntity> flight = repo.findByFlightNumber(flightProxy.getFlightNumber());

		// Check if the flight does not exist (empty Optional)
		if (flight.isEmpty()) {
			// Create a new FlightEntity and set the fields
			FlightEntity newFlight = MapperUtil.convertValue(flightProxy, FlightEntity.class);
			newFlight.setCreatedAt(new Date());
			newFlight.setUpdatedAt(new Date());

			// Save the new flight
			repo.save(newFlight);
			return "Flight data saved successfully!!";
		}

		return "Flight already exists with the given flight number.";
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
		Optional<FlightEntity> flight = repo.findByFlightNumber(flightProxy.getFlightNumber());

		if (flight.isPresent()) {
			// Update flight details if not null in the FlightProxy
			if (flightProxy.getDepartureDate() != null)
				flight.get().setDepartureDate(flightProxy.getDepartureDate());

			if (flightProxy.getDepartureTime() != null)
				flight.get().setDepartureTime(flightProxy.getDepartureTime());

			if (flightProxy.getArrivalDate() != null)
				flight.get().setArrivalDate(flightProxy.getArrivalDate());

			if (flightProxy.getArrivalTime() != null)
				flight.get().setArrivalTime(flightProxy.getArrivalTime());

			if (flightProxy.getDepartureAirport() != null)
				flight.get().setDepartureAirport(flightProxy.getDepartureAirport());

			if (flightProxy.getArrivalAirport() != null)
				flight.get().setArrivalAirport(flightProxy.getArrivalAirport());

			if (flightProxy.getPrice() != null)
				flight.get().setPrice(flightProxy.getPrice());

			if (flightProxy.getSeatsAvailable() != null)
				flight.get().setSeatsAvailable(flightProxy.getSeatsAvailable());

			if (flightProxy.getDurationMinutes() != null)
				flight.get().setDurationMinutes(flightProxy.getDurationMinutes());

//			if (flightProxy.getAirlineName() != null)
//				flight.get().setAirlineName(flightProxy.getAirlineName());

			if (flightProxy.getFlightClass() != null)
				flight.get().setFlightClass(flightProxy.getFlightClass());

			flight.get().setUpdatedAt(new Date());

			// Save the updated flight
			repo.save(flight.get());

			return "Flight details updated successfully.";
		}

		return "Flight not found.";
	}

	@Override
	public String deleteFlightDetails(String flightNumber) {
		// TODO Auto-generated method stub
		Optional<FlightEntity> flight = repo.findByFlightNumber(flightNumber);

		if (flight.isPresent()) {
			repo.delete(flight.get());
			return "Flight record deleted successsfully.";

		}
		return null;
	}

	@Override
	public List<FlightProxy> getFlightDetailsByUserDetails(FlightSearchProxy flightSearchProxy) {
		// TODO Auto-generated method stub
//		System.err.println(flightSearchProxy);
//		Optional<List<FlightEntity>> flightList = repo
//				.findByDepartureAirportAndArrivalAirportAndDepartureDateAndArrivalDateAndFlightClass(
//						flightSearchProxy.getDepartureAirport(), flightSearchProxy.getArrivalAirport(),
//						flightSearchProxy.getDepartureDate(), flightSearchProxy.getArrivalDate(),
//						flightSearchProxy.getFlightClass());
//		System.err.println(flightList.get());
//		if (flightList.isPresent()) {
//			return MapperUtil.convertListofValue(flightList.get().stream()
//					.filter(obj -> obj.getSeatsAvailable() >= flightSearchProxy.getPersonCount())
//					.collect(Collectors.toList()), FlightProxy.class);
//		}
//		return null;

//		System.err.println(flightSearchProxy);
//
//		List<FlightEntity> flightList = repo
//				.findByDepartureAirportAndArrivalAirportAndDepartureDateAndArrivalDateAndFlightClass(
//						flightSearchProxy.getDepartureAirport(), flightSearchProxy.getArrivalAirport(),
//						flightSearchProxy.getDepartureDate(), flightSearchProxy.getArrivalDate(),
//						flightSearchProxy.getFlightClass());
//
//		if (!flightList.isEmpty()) {
//			System.err.println(flightList);
//			return MapperUtil.convertListofValue(
//					flightList.stream().filter(obj -> obj.getSeatsAvailable() >= flightSearchProxy.getPersonCount())
//							.collect(Collectors.toList()),
//					FlightProxy.class);
//		}
//		return null;

//		System.err.println(flightSearchProxy);

		ZoneId zoneId = ZoneId.of("Asia/Kolkata");
		LocalDate today = LocalDate.now(zoneId);
		LocalTime nowTime = LocalTime.now(zoneId);

		// Convert java.util.Date to LocalDate in Indian time zone
		LocalDate depLocalDate = flightSearchProxy.getDepartureDate().toInstant().atZone(zoneId)
				.toLocalDate();

		// If user searched for a past date, return no flights immediately
		if (depLocalDate.isBefore(today)) {
			return null;
		}

		// Create start and end of day
		Date depStart = Date.from(depLocalDate.atStartOfDay(zoneId).toInstant());
		Date depEnd = Date.from(depLocalDate.atTime(23, 59, 59).atZone(zoneId).toInstant());

		List<FlightEntity> flightList;
		if (flightSearchProxy.getArrivalDate() != null) {
			LocalDate arrLocalDate = flightSearchProxy.getArrivalDate().toInstant().atZone(zoneId)
					.toLocalDate();
			Date arrStart = Date.from(arrLocalDate.atStartOfDay(zoneId).toInstant());
			Date arrEnd = Date.from(arrLocalDate.atTime(23, 59, 59).atZone(zoneId).toInstant());

			flightList = repo.findFlightsInRange(flightSearchProxy.getDepartureAirport(),
					flightSearchProxy.getArrivalAirport(), depStart, depEnd, arrStart, arrEnd,
					flightSearchProxy.getFlightClass());
		} else {
			flightList = repo.findFlightsByDeparture(flightSearchProxy.getDepartureAirport(),
					flightSearchProxy.getArrivalAirport(), depStart, depEnd,
					flightSearchProxy.getFlightClass());
		}

		if (flightList != null && !flightList.isEmpty()) {
			List<FlightEntity> activeFutureFlights = flightList.stream()
					.filter(obj -> obj.getSeatsAvailable() != null && obj.getSeatsAvailable() >= flightSearchProxy.getPersonCount())
					.filter(obj -> isFlightInFuture(obj, depLocalDate, today, nowTime))
					.collect(Collectors.toList());

			if (!activeFutureFlights.isEmpty()) {
				return MapperUtil.convertListofValue(activeFutureFlights, FlightProxy.class);
			}
		}
		return null;
	}

	private boolean isFlightInFuture(FlightEntity flight, LocalDate depLocalDate, LocalDate today, LocalTime nowTime) {
		// If flight is scheduled on a future date, it is in the future
		if (depLocalDate.isAfter(today)) {
			return true;
		}

		// If flight is scheduled for today, check its scheduled departure time
		if (flight.getDepartureTime() != null && !flight.getDepartureTime().trim().isEmpty()) {
			String rawTime = flight.getDepartureTime().trim().toUpperCase()
					.replaceAll("([0-9])([AP]M)", "$1 $2");
			String[] patterns = { "h:mm a", "hh:mm a", "H:mm", "HH:mm", "h:mm:ss a", "H:mm:ss" };
			for (String p : patterns) {
				try {
					LocalTime flightTime = LocalTime.parse(rawTime, DateTimeFormatter.ofPattern(p, Locale.ENGLISH));
					return flightTime.isAfter(nowTime);
				} catch (Exception ignored) {
				}
			}
		}

		// Fallback to departureDate timestamp if departureTime string parsing was inconclusive
		if (flight.getDepartureDate() != null) {
			return flight.getDepartureDate().toInstant().isAfter(Instant.now());
		}

		return true;
	}

	@Override
	public Map<String, Object> getAvailableAirports() {
		List<AirportRouteProjection> routeProjections = repo.findDistinctAirportRoutes();
		Set<String> departures = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		Set<String> arrivals = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		Set<String> allAirports = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		Map<String, Set<String>> routes = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

		for (AirportRouteProjection r : routeProjections) {
			if (r.getDepartureAirport() != null && !r.getDepartureAirport().trim().isEmpty()) {
				String dep = capitalizeWords(r.getDepartureAirport().trim());
				departures.add(dep);
				allAirports.add(dep);

				if (r.getArrivalAirport() != null && !r.getArrivalAirport().trim().isEmpty()) {
					String arr = capitalizeWords(r.getArrivalAirport().trim());
					arrivals.add(arr);
					allAirports.add(arr);

					routes.computeIfAbsent(dep, k -> new TreeSet<>(String.CASE_INSENSITIVE_ORDER)).add(arr);
				}
			}
		}

		Map<String, Object> result = new HashMap<>();
		result.put("airports", new ArrayList<>(allAirports));
		result.put("departureAirports", new ArrayList<>(departures));
		result.put("arrivalAirports", new ArrayList<>(arrivals));
		result.put("routes", routes);
		return result;
	}

	private String capitalizeWords(String str) {
		if (str == null || str.isEmpty()) return str;
		String[] words = str.toLowerCase().split("\\s+");
		StringBuilder sb = new StringBuilder();
		for (String w : words) {
			if (!w.isEmpty()) {
				sb.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1)).append(" ");
			}
		}
		return sb.toString().trim();
	}

    @Override
    public Response downloadAllFlightData() {
        try {
            log.info("Entry into downloadAllFlightData()");

            List<FlightEntity> flights = repo.findAll();
            SXSSFWorkbook workbook = new SXSSFWorkbook(100);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Sheet sheet = workbook.createSheet("Flights");
            Row headerRow = sheet.createRow(0);

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);

            String[] headers = {"Sr. No.", "Flight Number", "Departure Date", "Departure Time", "Arrival Date", "Arrival Time", "Departure Airport", "Arrival Airport", "Price", "Seats Available", "Duration (Min)", "Flight Class", "Created At", "Updated At"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (FlightEntity flight : flights) {
                Row row = sheet.createRow(rowNum);
                row.createCell(0).setCellValue(rowNum);
                row.createCell(1).setCellValue(flight.getFlightNumber());
                row.createCell(2).setCellValue(flight.getDepartureDate().toString());
                row.createCell(3).setCellValue(flight.getDepartureTime());
                row.createCell(4).setCellValue(flight.getArrivalDate().toString());
                row.createCell(5).setCellValue(flight.getArrivalTime());
                row.createCell(6).setCellValue(flight.getDepartureAirport());
                row.createCell(7).setCellValue(flight.getArrivalAirport());
                row.createCell(8).setCellValue(currencyFormatter.format(flight.getPrice()));
                row.createCell(9).setCellValue(flight.getSeatsAvailable());
                row.createCell(10).setCellValue(flight.getDurationMinutes());
                row.createCell(11).setCellValue(flight.getFlightClass());
                row.createCell(12).setCellValue(flight.getCreatedAt().toString());
                row.createCell(13).setCellValue(flight.getUpdatedAt().toString());
                rowNum++;
            }


            workbook.write(outputStream);
            workbook.close();

            log.info("Excel file downloaded successfully for all flight data !!");

            return Response.builder().data(outputStream.toByteArray()).message("All flight data downloaded successfully !")
                    .status_code(HttpStatus.OK.toString()).build();
        } catch (Exception e) {
            log.error("Error generating excel: {}", e.getMessage(), e);
            return Response.builder().message("Error generating excel file")
                    .status_code(HttpStatus.INTERNAL_SERVER_ERROR.toString()).build();
        }
    }
}
