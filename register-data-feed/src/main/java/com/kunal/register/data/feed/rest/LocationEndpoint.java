package com.kunal.register.data.feed.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import com.kunal.register.data.feed.bean.CamelRoute;
import com.kunal.register.data.feed.model.Location;

/**
 * 
 */
@Stateless
@Path("/locations")
public class LocationEndpoint {
	@PersistenceContext(unitName = "forge-default")
	private EntityManager em;

	@Inject
	private CamelRoute camelRoute;

	@POST
	@Consumes("application/json")
	@Path("/new/{location}")
	public Response create(@PathParam("location") Location location) throws Exception {
		
		camelRoute.configure(
				//Need to use string concat 
				location.getLeftAsLongitude() + ""
				+ location.getBottomAsLatitude() + ""
				+location.getRightAsLongitude() + ""
				+ location.getTopAsLatitude() + ""
				);
		em.persist(location);
		return Response.created(
				UriBuilder.fromResource(LocationEndpoint.class)
						.path(String.valueOf(location.getId())).build()).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		Location entity = em.find(Location.class, id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		em.remove(entity);
		return Response.noContent().build();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json")
	public Response findById(@PathParam("id") Long id) {
		TypedQuery<Location> findByIdQuery = em
				.createQuery(
						"SELECT DISTINCT l FROM Location l WHERE l.id = :entityId ORDER BY l.id",
						Location.class);
		findByIdQuery.setParameter("entityId", id);
		Location entity;
		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(entity).build();
	}

	@GET
	@Produces("application/json")
	public List<Location> listAll() {
		final List<Location> results = em.createQuery(
				"SELECT DISTINCT l FROM Location l ORDER BY l.id",
				Location.class).getResultList();
		return results;
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(Location entity) {
		entity = em.merge(entity);
		return Response.noContent().build();
	}
}