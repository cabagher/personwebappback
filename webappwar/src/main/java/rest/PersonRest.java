package rest;

import java.util.List;
import java.util.Optional;

import entity.Person;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import services.PersonService;

@Path("/person")
public class PersonRest {

	@Inject
	PersonService ps;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "/{id}")
	public Response getPersonById(@PathParam("id") String id) {
		Optional<Person> person = ps.findPerson(id);
		if (person.isEmpty()) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity("The id is invalid or the resource could not be found: " + id).build();
		}
		return Response.status(Response.Status.OK).entity(person.get()).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "/getAll")
	public Response getAll() {
		return Response.status(Response.Status.OK).entity(ps.findAll()).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "/create")
	public Response createPerson(Person person) {
		Optional<Person> personOptional = ps.addPerson(person);
		if (personOptional.isEmpty()) {
			return Response.status(Response.Status.CONFLICT)
					.entity("Resource invalid or already exists: " + person.toString()).build();
		}
		return Response.status(Response.Status.CREATED).entity(person).build();
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "/delete/{id}")
	public Response deletePerson(@PathParam("id") Integer id) {
		boolean isPersonDeleted = ps.deletePerson(id);
		if (!isPersonDeleted) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity("Resource may not exist. Could not remove resource with ID: " + id ).build();
		}
		return Response.status(Response.Status.NO_CONTENT).build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "/edit/{id}")
	public Response editPerson(@PathParam("id") Integer id, Person person) {
		boolean personEdited =ps.editPerson(person);
		if (!personEdited) {
			return Response.status(Response.Status.CONFLICT)
					.entity("Resource may not exist. Could not edit resource with ID: " + id ).build();
		}
		return Response.status(Response.Status.OK).build();
	}
}
