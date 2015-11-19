package rest.authorization;

import jetty_server.jetty_server;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.FormParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;

/**
 * Created by user on 15.11.15.
 */
@Path("/auth")
public class authorize {

    @GET
    @Path("/user")
    @Produces("text/html")
    public Response authorizetionUser()
    {
        try {
            return  Response.ok(jetty_server.html.GetAutho(), MediaType.TEXT_HTML_TYPE).build();
        }catch (Exception ex)
        {
            return Response.noContent().build();
        }
    }

    @GET
    @Path("/usersss")
    @Produces("text/html")
    public Response me()
    {
        try {
            return  Response.ok(jetty_server.html.GetAutho(), MediaType.TEXT_HTML_TYPE).build();
        }catch (Exception ex)
        {
            return Response.noContent().build();
        }
    }


    @GET
    @Path("/users")
 //   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response getFormauthorizetionUser(@QueryParam("login") String login,
                                             @QueryParam("passwd") String passwd)
    {
        try {
            return  Response.ok(jetty_server.html.GetAutho(), MediaType.TEXT_HTML_TYPE).build();
        }catch (Exception ex)
        {
            return Response.noContent().build();
        }
    }

}
