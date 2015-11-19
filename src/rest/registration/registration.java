package rest.registration;

import jetty_server.jetty_server;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 15.11.15.
 */
@Path("/regis")
public class registration {

    String[] userFields = {"login", "passwd", "name", "address"};

    @GET
    @Path("/user")
    @Produces("text/html")
    public Response getRegistrationForm()
    {

        try {
            return  Response.ok(jetty_server.html.GetRegis(), MediaType.TEXT_HTML_TYPE).build();
        }catch (Exception ex)
        {
            return Response.noContent().build();
        }
    }

    @GET
    @Path("/param")
    @Produces("text/html")
    public Response registreationUser(@Context HttpServletRequest httpRequest)
    {
        Map<String , String> map = jetty_server.oath.cheakFields(httpRequest.getQueryString(), this.userFields);

        if(map != null)
        {
            if(jetty_server.oath.Registration(map))
                return Response.status(Response.Status.CREATED).type("text/plain").entity("created").build();
        }

        return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity("bad request").build();
    }
}
