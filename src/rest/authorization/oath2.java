package rest.authorization;

import com.sun.net.httpserver.HttpContext;
import jetty_server.jetty_server;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by user on 15.11.15.
 */
@Path("/oath")
public class oath2 {

    @GET
    @Path("/authorize")
    @Produces("text/html")
    public Response getCode(@QueryParam("client_id") String client_id,
                            @QueryParam("redirect_uri") String redirect_uri,
                            @QueryParam("login") String login,
                            @QueryParam("passwd") String passwd,
                            @Context HttpServletResponse _currentResponse)
    {
        if(login == null && passwd == null)
        {
            try {

                    return Response.ok(jetty_server.html.GetAutho(), MediaType.TEXT_HTML_TYPE)
                            .cookie(new NewCookie("client_id", client_id))
                            .cookie(new NewCookie("redirect_uri", redirect_uri)).build();
            }catch (Exception ex)
            {

            }
        }else
        {
            try {
                if (jetty_server.oath.checkUser(login, passwd)
                        && jetty_server.oath.checkClientId(client_id)
                        && redirect_uri != null) {

                    Map<String, String> map = jetty_server.oath.workCodeSate(login);

                    if (map.get("code") != null && map.get("state") != null) {

                        if (!redirect_uri.contains("http://"))
                            _currentResponse.sendRedirect("https://" + redirect_uri + "?code=" + map.get("code") + "?state=" + map.get("sate"));
                        else
                            _currentResponse.sendRedirect(redirect_uri + "?code=" + map.get("code") + "?state=" + map.get("sate"));
                    }


                }
            }catch (Exception ex)
            {
            }
            return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity("Bad Request").build();

        }
        return Response.ok().build();
    }

    @GET
    @Path("/user")
    @Produces("text/html")
    public Response CheakUser(@QueryParam("login") String login,
                              @QueryParam("passwd") String passwd,
                              @CookieParam("client_id") String client_id,
                              @CookieParam("redirect_uri") String redirect_uri,
                              @Context HttpServletRequest httpRequest,
                              @Context HttpServletResponse _currentResponse,
                              @Context HttpServletRequest _currentRequest,
                              @Context ServletContext _context)
    {
        try {
            if (jetty_server.oath.checkUser(login, passwd)
                    && jetty_server.oath.checkClientId(client_id)
                    && redirect_uri != null)
            {
                Map<String, String> map = jetty_server.oath.workCodeSate(login);

                if (map.get("code") != null && map.get("state") != null) {

                //    Pattern p = Pattern.compile("http://");
                 //   Matcher m = p.matcher(redirect_uri);

                    if(!redirect_uri.contains("http://"))
                        _currentResponse.sendRedirect("https://" + redirect_uri + "?code=" + map.get("code") + "&state=" + map.get("state"));
                    else
                        _currentResponse.sendRedirect(redirect_uri + "?code=" + map.get("code") + "&state=" + map.get("state"));


                     /*  URI url = new URI("http://" + redirect_uri + "?code=" + code);
                        return Response.ok().location(url).build();
                        */
                    //    RequestDispatcher dispatcher =  _context.getRequestDispatcher(url.toString());
                    //  _currentRequest.setAttribute("code", code);

                 //   dispatcher.forward(_currentRequest, _currentResponse);

                   // return Response.seeOther(url).build();
                   // if(m.matches())
                  //      _currentResponse.sendRedirect(redirect_uri + "?code=" + code);
                  //  else
                  //  _currentResponse.
                      //  _currentResponse.sendRedirect("http://" + redirect_uri + "?code=" + code);
                    //    URI url = new URI("http://" + redirect_uri + "?code=" + code);
             //       return Response.temporaryRedirect(url).build();
                }
            }
        }catch (Exception ex)
        {

        }
        return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity("Bad Request").build();
    }


    @POST
    @Path("/access_token")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccessToken(@QueryParam("secret_client") String secret_client,
                                   @QueryParam("client_id") String client_id,
                                   @QueryParam("code") String code,
                                   @QueryParam("state") int state,
                                   @Context HttpServletRequest httpRequest,
                                   @Context HttpServletResponse _currentResponse)
    {
        try {
            if (jetty_server.oath.checkClientId(client_id)
                    && jetty_server.oath.checkSecretClient(secret_client)) {

                String access_token = jetty_server.oath.getAccessToken(code, state);

                if (access_token != null) {
                    return  Response.ok(new JSONObject().put("access_token", access_token).toString(),
                            MediaType.APPLICATION_JSON).build();
                }

            }
        }catch (Exception ex)
        {

        }
        return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity("Bad Request").build();
    }

}
