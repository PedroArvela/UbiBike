package pt.tecnico.cmu.resources;

import java.net.URI;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import pt.tecnico.cmu.UbiManager;

@Path("user")
public class UserResource {
	@Context
	UriInfo uri;

	/**
	 * Create new user
	 * 
	 * @param username
	 *            The id of the new user in the form of a query (
	 *            "?username=<user>")
	 * @return HTTP status response. 201 created for success. 403 forbidden if
	 *         the user already exists.
	 */
	@PUT
	public Response create(@QueryParam("username") String username) {
		UbiManager manager = UbiManager.getInstance();

		if (!manager.userExists(username)) {
			manager.createUser(username);

			URI userUri = URI.create(uri.getBaseUri() + "user/" + username);
			return Response.created(userUri).build();
		} else {
			return Response.status(Status.FORBIDDEN).build();
		}
	}
}
