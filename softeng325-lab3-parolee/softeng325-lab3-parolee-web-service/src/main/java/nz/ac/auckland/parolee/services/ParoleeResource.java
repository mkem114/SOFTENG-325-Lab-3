package nz.ac.auckland.parolee.services;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nz.ac.auckland.parolee.domain.Address;
import nz.ac.auckland.parolee.domain.CriminalProfile;
import nz.ac.auckland.parolee.domain.Curfew;
import nz.ac.auckland.parolee.domain.Gender;
import nz.ac.auckland.parolee.domain.GeoPosition;
import nz.ac.auckland.parolee.domain.Movement;
import nz.ac.auckland.parolee.domain.Parolee;
import nz.ac.auckland.parolee.domain.CriminalProfile.Offence;

/**
 * Web service resource implementation for the Parolee application. An instance
 * of this class handles all HTTP requests for the Parolee Web service.
 *
 */
@Path("/parolees")
public class ParoleeResource {
	private static final Logger _logger = LoggerFactory.getLogger(ParoleeResource.class);
	
	private Map<Long, Parolee> _paroleeDB;
	private AtomicLong _idCounter;

	public ParoleeResource() {
		reloadDatabase();
	}

	/**
    * Convenience method for testing the Web service. This method reinitialises 
    * the state of the Web service to hold three Parolee objects.
    */
	@PUT
	public void reloadData() {
		reloadDatabase();
	}

	/**
	 * Adds a new Parolee to the system. The state of the new Parolee is
	 * described by a nz.ac.auckland.parolee.dto.Parolee object.
	 * 
	 * @param dtoParolee
	 *            the Parolee data included in the HTTP request body.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	public Response createParolee(
			nz.ac.auckland.parolee.dto.Parolee dtoParolee) {

		Parolee parolee = ParoleeMapper.toDomainModel(dtoParolee);
		parolee.setId(_idCounter.incrementAndGet());
		_paroleeDB.put(parolee.getId(), parolee);
		
		// Return a Response that specifies a status code of 201 Created along
		// with the Location header set to the URI of the newly created Parolee.
		return Response.created(URI.create("/parolees/" + parolee.getId()))
				.build();
	}

	/**
	 * Records a new Movement for a particular Parolee.
	 * 
	 * @param id
	 *            the unique identifier of the Parolee.
	 * @param movement
	 *            the timestamped latitude/longitude position of the Parolee.
	 * 
	 */
	@POST
	@Path("{id}/movements")
	@Consumes(MediaType.APPLICATION_XML)
	public void createMovementForParolee(@PathParam("id") long id,
			Movement movement) {
		Parolee parolee = findParolee(id);
		parolee.addMovement(movement);
		
		// JAX-RS will add the default response code to the HTTP response 
		// message.
	}

	/**
	 * Updates an existing Parolee. The parts of a Parolee that can be updated
	 * are those represented by a nz.ac.auckland.parolee.dto.Parolee
	 * instance.
	 * 
	 * @param dtoParolee
	 *            the Parolee data included in the HTTP request body.
	 */
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_XML)
	public void updateParolee(
			nz.ac.auckland.parolee.dto.Parolee dtoParolee) {
		// Get the Parolee object from the database.
		Parolee parolee = findParolee(dtoParolee.getId());

		// Update the Parolee object in the database based on the data in
		// dtoParolee.
		parolee.setFirstname(dtoParolee.getFirstname());
		parolee.setLastname(dtoParolee.getLastname());
		parolee.setGender(dtoParolee.getGender());
		parolee.setDateOfBirth(dtoParolee.getDateOfBirth());
		parolee.setHomeAddress(dtoParolee.getHomeAddress());
		parolee.setCurfew(dtoParolee.getCurfew());
		
		// JAX-RS will add the default response code (204 No Content) to the
		// HTTP response message.
	}
	
	/**
	 * Updates the set of a dissassociate Parolees for a given Parolee.
	 * 
	 * @param id the Parolee whose dissassociates should be updated.
	 * 
	 * @param dissassociates the new set of dissassociates.
	 */
	@PUT
	@Path("{id}/dissassociates")
	@Consumes(MediaType.APPLICATION_XML)
	public void updateDissassociates(@PathParam("id") long id, Set<nz.ac.auckland.parolee.dto.Parolee> dissassociates) {
		// Get the Parolee object from the database.
		Parolee parolee = findParolee(id);
		
		// Lookup the dissassociate Parolee instances in the database.
		Set<Parolee> dissassociatesInDatabase = new HashSet<Parolee>();
		for(nz.ac.auckland.parolee.dto.Parolee dtoParolee : dissassociates) {
			Parolee dissassociate = findParolee(dtoParolee.getId());
			dissassociatesInDatabase.add(dissassociate);
		}
		
		// Update the Parolee by setting its dissassociates.
		parolee.updateDissassociates(dissassociatesInDatabase);
		
		// JAX-RS will add the default response code (204 No Content) to the
		// HTTP response message.
	}
	
	/**
	 * Updates a Parolee's CriminalProfile.
	 * 
	 * @param id the unique identifier of the Parolee.
	 * 
	 * @param profile the Parolee's updated criminal profile.
	 */
	@PUT
	@Path("{id}/criminal-profile")
	@Consumes(MediaType.APPLICATION_XML)
	public void updateCriminalProfile(@PathParam("id") long id, CriminalProfile profile) {
		// Get the Parolee object from the database.
		Parolee parolee = findParolee(id);
		
		// Update the Parolee's criminal profile.
		parolee.setCriminalProfile(profile);
		
		// JAX-RS will add the default response code (204 No Content) to the
		// HTTP response message.
	}

	/**
	 * Returns a particular Parolee. The returned Parolee is represented by a
	 * nz.ac.auckland.parolee.dto.Parolee object.
	 * 
	 * @param id
	 *            the unique identifier of the Parolee.
	 * 
	 */
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_XML)
	public nz.ac.auckland.parolee.dto.Parolee getParolee(
			@PathParam("id")long id) {
		// Get the Parolee object from the database.
		Parolee parolee = findParolee(id);

		// Convert the Parolee to a Parolee DTO.
		nz.ac.auckland.parolee.dto.Parolee dtoParolee = ParoleeMapper.toDto(parolee);
		
		// JAX-RS will processed the returned value, marshalling it and storing
		// it in the HTTP response message body. It will use the default status 
		// code of 200 Ok.
		return dtoParolee;
	}

	/**
	 * Returns a view of the Parolee database, represented as a List of
	 * nz.ac.auckland.parolee.dto.Parolee objects.
	 * 
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getParolees(@DefaultValue("1") @QueryParam("start") int start, 
			@DefaultValue("1") @QueryParam("size")int size,
			@Context UriInfo uriInfo) {
		URI uri = uriInfo.getAbsolutePath();
		
		Link previous = null;
		Link next = null;
		
		if(start > 1) {
			// There are previous Parolees - create a previous link.
			previous = Link.fromUri(uri + "?start={start}&size={size}")
					.rel("prev")
					.build(start - 1, size);
		}
		if(start + size <= _paroleeDB.size()) {
			// There are successive parolees - create a next link.
			next = Link.fromUri(uri + "?start={start}&size={size}")
					.rel("next")
					.build(start + 1, size);
		}

		// Create list of Parolees to return.
		List<nz.ac.auckland.parolee.dto.Parolee> parolees = 
				new ArrayList<nz.ac.auckland.parolee.dto.Parolee>();
		long paroleeId = start;
		for(int i = 0; i < size; i++) {
			Parolee parolee = _paroleeDB.get(paroleeId);
			parolees.add(ParoleeMapper.toDto(parolee));
		}
		
		// Create a GenericEntity to wrap the list of Parolees to return. This
		// is necessary to preserve generic type data when using any
		// MessageBodyWriter to handle translation to a particular data format.
		GenericEntity<List<nz.ac.auckland.parolee.dto.Parolee>> entity = 
				new GenericEntity<List<nz.ac.auckland.parolee.dto.Parolee>>(parolees) {};
		
		// Build a Response that contains the list of Parolees plus the link 
		// headers.
 		ResponseBuilder builder = Response.ok(entity);
 		if(previous != null) {
 			builder.links(previous);
 		}
 		if(next != null) {
 			builder.links(next);
 		}
 		Response response = builder.build();
 		
 		// Return the custom Response. The JAX-RS run-time will process this,
 		// extracting the List of Parolee objects and marshalling them into the
 		// HTTP response message body. In addition, since the Response object
 		// contains headers (previous and/or next), these will be added to the 
 		// HTTP response message. The Response object was created with the 200
 		// Ok status code, and this too will be added for the status header.
 		return response;
	}

	/**
	 * Returns movement history for a particular Parolee.
	 * 
	 * @param id
	 *            the unique identifier of the Parolee.
	 * 
	 */
	@GET
	@Path("{id}/movements")
	@Produces(MediaType.APPLICATION_XML)
	public List<Movement> getMovements(@PathParam("id") long id) {
		// Get the Parolee object from the database.
		Parolee parolee = findParolee(id);

		// Return the Parolee's movements.
		return parolee.getMovements();
		
		// JAX-RS will processed the returned value, marshalling it and storing
		// it in the HTTP response message body. It will use the default status 
		// code of 200 Ok.
	}

	/**
	 * Returns the dissassociates associated directly with a particular Parolee.
	 * Each dissassociate is represented as an instance of class
	 * nz.ac.auckland.parolee.dto.Parolee.
	 * 
	 * @param id
	 *            the unique identifier of the Parolee.
	 */
	@GET
	@Path("{id}/dissassociates")
	@Produces(MediaType.APPLICATION_XML)
	public List<nz.ac.auckland.parolee.dto.Parolee> getParoleeDissassociates(
			@PathParam("id") long id) {
		// Get the Parolee object from the database.
		Parolee parolee = findParolee(id);

		List<nz.ac.auckland.parolee.dto.Parolee> dissassociates = new ArrayList<nz.ac.auckland.parolee.dto.Parolee>();

		for (Parolee dissassociate : parolee.getDissassociates()) {
			dissassociates.add(ParoleeMapper.toDto(dissassociate));
		}
		return dissassociates;
		
		// JAX-RS will process the returned value, marshalling it and storing
		// it in the HTTP response message body. It will use the default status 
		// code of 200 Ok.
	}

	/**
	 * Returns the CriminalProfile for a particular Parolee.
	 * 
	 * @param id the unique identifier of the Parolee.
	 */
	@GET
	@Path("{id}/criminal-profile")
	@Produces(MediaType.APPLICATION_XML)
	public CriminalProfile getCriminalProfile(@PathParam("id") long id) {
		// Get the Parolee object from the database.
		Parolee parolee = findParolee(id);
		
		return parolee.getCriminalProfile();
		
		// JAX-RS will processed the returned value, marshalling it and storing
		// it in the HTTP response message body. It will use the default status 
		// code of 200 Ok.
	}
	
	
	protected Parolee findParolee(long id) {
		return _paroleeDB.get(id);
	}

	protected void reloadDatabase() {
		_paroleeDB = new ConcurrentHashMap<Long, Parolee>();
		_idCounter = new AtomicLong();

		// === Initialise Parolee #1
		long id = _idCounter.incrementAndGet();
		Address address = new Address("15", "Bermuda road", "St Johns", "Auckland", "1071");
		Parolee parolee = new Parolee(id,
				"Sinnen", 
				"Oliver", 
				Gender.MALE,
				new LocalDate(1970, 5, 26),
				address,
				new Curfew(address, new LocalTime(20, 00),new LocalTime(06, 30)));
		_paroleeDB.put(id, parolee);

		CriminalProfile profile = new CriminalProfile();
		profile.addConviction(new CriminalProfile.Conviction(new LocalDate(
				1994, 1, 19), "Crime of passion", Offence.MURDER,
				Offence.POSSESION_OF_OFFENSIVE_WEAPON));
		parolee.setCriminalProfile(profile);

		LocalDateTime now = new LocalDateTime();
		LocalDateTime earlierToday = now.minusHours(1);
		LocalDateTime yesterday = now.minusDays(1);
		GeoPosition position = new GeoPosition(-36.852617, 174.769525);

		parolee.addMovement(new Movement(yesterday, position));
		parolee.addMovement(new Movement(earlierToday, position));
		parolee.addMovement(new Movement(now, position));
		
		// === Initialise Parolee #2
		id = _idCounter.incrementAndGet();
		address = new Address("22", "Tarawera Terrace", "St Heliers", "Auckland", "1071");
		parolee = new Parolee(id,
				"Watson", 
				"Catherine", 
				Gender.FEMALE,
				new LocalDate(1970, 2, 9),
				address,
				null);
		_paroleeDB.put(id, parolee);
		
		// === Initialise Parolee #3
		id = _idCounter.incrementAndGet();
		address = new Address("67", "Drayton Gardens", "Oraeki", "Auckland", "1071");
		parolee = new Parolee(id,
				"Giacaman", 
				"Nasser", 
				Gender.MALE,
				new LocalDate(1980, 10, 19),
				address,
				null);
		_paroleeDB.put(id, parolee);
	}
}
