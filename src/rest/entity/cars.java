package rest.entity;

/**
 * Created by user on 29.10.15.
 */

import Authorization.Authorization;
import org.eclipse.jetty.server.Request;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.QueryParam;
import jetty_server.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URI;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@Path("/api")
public class cars {

    public String[] carsFields = {"model", "power", "mileage", "year", "color", "drive", "info", "cost", "marks_id"};

    public String[] marksFields = {"name", "contry", "info"};

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @HttpMethod("PATCH")
    public @interface PATCH
    {
    }



    private JSONObject JSONArrayToJSONObject(JSONArray array, String field)
    {
        JSONObject json = new JSONObject();

        for(int i = 0; i < array.length(); i++)
            json.append(field, array.get(i));

        return json;
    }

    private JSONObject createJsonAsList(int page, int per_page, JSONArray array, String nameDB, String field)
    {
        JSONObject json = new JSONObject();

        for(int i = 0; i < array.length(); i++)
            json.append(field, array.get(i));

        int total = jetty_server.db.countRecord(nameDB);

        int pages = (int)Math.ceil((double)total / (double)per_page);

        json.put("page", page);
        json.put("per_page", per_page);
        json.put("pages", pages);
        json.put("total", total);

        if((page >= pages) || (page > 2000 / per_page))
            return null;

        return json;
    }

    private JSONObject createJson(JSONArray array, String field)
    {
        JSONObject json = new JSONObject();

        for(int i = 0; i < array.length(); i++)
            json.append(field, array.get(i));

        return json;
    }


    //region API автомобилей

    @GET
    @Path("/cars")
    @Produces(MediaType.APPLICATION_JSON)
    public Response  getCars(@QueryParam("page") int page,
                             @DefaultValue("20") @QueryParam("per_page") int per_page,
                             @Context HttpServletRequest httpRequest)
    {
        JSONObject json = null;
        String access_token = httpRequest.getHeader("access_token");

        try
        {
         //   if(jetty_server.db.checkAccessToken(access_token).next())
         //   {
                try {

                    ResultSet records = jetty_server.db.GetAllCars(per_page, page * per_page);
                    JSONArray resultJson = jetty_server.oath.convert(records);

                    json = this.createJsonAsList(page, per_page, resultJson, "cars", "cars");

                } catch (Exception ex) {
                    System.out.print("Ошибка");
                }

                if(json != null)
                    return Response.ok(json.toString(), MediaType.APPLICATION_JSON).build();

                return Response.status(Response.Status.NOT_FOUND).build();

        //    }else
        //    {
        //        return Response.status(Response.Status.FORBIDDEN).build();
         //   }
        }catch (Exception ex)
        {
            return Response.serverError().build();
        }

    }



    @GET
    @Path("/cars/{id}")
    @Produces("text/plain")
    public Response  getCar(@PathParam("id") int id,
                             @Context HttpServletRequest httpRequest)
    {
        JSONObject json = new JSONObject();
        String access_token = httpRequest.getHeader("access_token");
        JSONArray resultJson = null;
        try
        {
            if(jetty_server.db.checkAccessToken(access_token).next())
            {
                try {

                    ResultSet res = jetty_server.db.GetCarFull((int) id);
                    resultJson = jetty_server.oath.convert(res);
                    json.append("cars", resultJson);

                } catch (Exception ex) {
                    System.out.print("Ошибка");
                }

                if(json.length() > 0 && resultJson.length() > 0)
                    return Response.ok(json.toString(), MediaType.APPLICATION_JSON).build();

                return Response.status(Response.Status.NOT_FOUND).entity("404 Not Found").build();
                //return Response.status(Response.Status.NOT_FOUND).build();

            }else
            {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }catch (Exception ex)
        {
            return Response.serverError().build();
        }
    }


    @POST
    @Path("/cars")
    @Produces("text/plain")
    public Response  setCar(@QueryParam("model") String model,
                            @QueryParam("marks_id") int marks,
                            @QueryParam("power") int power,
                            @QueryParam("mileage") int mileage,
                            @QueryParam("year") int year,
                            @QueryParam("color") String color,
                            @QueryParam("drive") String drive,
                            @QueryParam("info") String info,
                            @QueryParam("cost") int cost,
                            @Context HttpServletRequest httpRequest)
    {
        String access_token = httpRequest.getHeader("access_token");
        try
        {
            if(jetty_server.db.checkAccessToken(access_token).next())
            {

                int num = jetty_server.db.SetNewCars(model, marks, power, mileage, year, color, drive, info, cost);
                if( num >= 0) {
                    URI uri = new  URI(httpRequest.getContextPath() + "/" + num);
                    return Response.created(uri).build();
                }
                return Response.status(Response.Status.BAD_REQUEST).build();

            }else
            {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }catch (Exception ex)
        {
            return Response.serverError().build();
        }
    }



    @PUT
    @Path("/cars/{id}")
    @Produces("text/plain")
    public Response  updateCar(@PathParam("id") int id,
                               @Context HttpServletRequest httpRequest)
    {
        String access_token = httpRequest.getHeader("access_token");
        try
        {
            if(jetty_server.db.checkAccessToken(access_token).next())
            {

                try {

                    Map<String, String> map = jetty_server.oath.queryToMap(httpRequest.getQueryString());
                    Map<String, String> map2 =  new HashMap<String, String>();

                    for(String str: this.carsFields)
                    {
                        if(map.get(str) != null)
                            map2.put(str, map.get(str));
                        else
                            return Response.status(Response.Status.BAD_REQUEST).build();
                    }

                    if(!jetty_server.db.GetCarFull(id).next())
                        return Response.status(Response.Status.NOT_FOUND).entity("404 Not Found").build();

                    if(jetty_server.db.updateCars(id, map2))
                        return Response.ok().build();

                } catch (Exception ex) {

                }
                return Response.serverError().build();
            }else
            {
                return Response.status(Response.Status.FORBIDDEN).type("text/plain").entity("403 Forbidden").build();
            }
        }catch (Exception ex) {
            return Response.serverError().build();
        }
    }

    @PATCH
    @Path("/cars/{id}")
    @Produces("text/plain")
    public Response  updateCarSomeFields(@PathParam("id") int id,
                                         @Context HttpServletRequest httpRequest)
    {
        String access_token = httpRequest.getHeader("access_token");
        try
        {
            if(jetty_server.db.checkAccessToken(access_token).next())
            {
                try {

                    Map<String, String> map = jetty_server.oath.queryToMap(httpRequest.getQueryString());
                    Map<String, String> map2 =  new HashMap<String, String>();

                    for(String str: this.carsFields)
                    {
                        if(map.get(str) != null)
                            map2.put(str, map.get(str));
                    }

                    if(!jetty_server.db.GetCarFull(id).next())
                        return Response.status(Response.Status.NOT_FOUND).entity("404 Not Found").build();

                    if(jetty_server.db.updateCars(id, map2))
                        return Response.ok().build();

                } catch (Exception ex) {
                    System.out.print("Ошибка");
                }

                return Response.serverError().build();
            }else
            {
                return Response.status(Response.Status.FORBIDDEN).entity("403 Forbidden").build();
            }
        }catch (Exception ex) {
            return Response.serverError().build();
        }
    }


    @DELETE
    @Path("/cars/{id}")
    @Produces("text/plain")
    public Response  deleteCar(@PathParam("id") int id,
                               @Context HttpServletRequest httpRequest)
    {
        String access_token = httpRequest.getHeader("access_token");
        try
        {
            if(jetty_server.db.checkAccessToken(access_token).next())
            {
                try {

                    if(!jetty_server.db.GetCarFull(id).next())
                        return Response.status(Response.Status.NOT_FOUND).entity("404 Not Found").build();

                    if(jetty_server.db.deleteCar(id))
                        return Response.status(204).build();

                } catch (Exception ex) {
                    System.out.print("Ошибка");
                }

                return Response.status(Response.Status.NOT_MODIFIED).build();
            }else
            {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }catch (Exception ex) {
            return Response.serverError().build();
        }
    }


    //endregion


    //region API марок автомобилей

    @GET
    @Path("/marks")
    @Produces(MediaType.APPLICATION_JSON)
    public Response  getMarks(@QueryParam("page") int page,
                              @DefaultValue("20") @QueryParam("per_page") int per_page,
                              @Context HttpServletRequest httpRequest)
    {
        JSONObject json = null;
        String access_token = httpRequest.getHeader("access_token");

        try
        {
            if(jetty_server.db.checkAccessToken(access_token).next())
            {
                try {

                    ResultSet res = jetty_server.db.GetAllMarks(per_page, page * per_page);
                    JSONArray resultJson = jetty_server.oath.convert(res);

                    json = createJsonAsList(page, per_page, resultJson, "marks", "marks");

                } catch (Exception ex) {
                    System.out.print("Ошибка");
                }

                if(json != null)
                    return Response.ok(json.toString(), MediaType.APPLICATION_JSON).build();

                return Response.status(Response.Status.NOT_FOUND).entity("404 Not Found").build();

            }else
            {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }catch (Exception ex)
        {
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/marks/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response  getMarks(@PathParam("id") int id,
                              @Context HttpServletRequest httpRequest)
    {
        JSONObject json = null;
        String access_token = httpRequest.getHeader("access_token");

        try
        {
            if(jetty_server.db.checkAccessToken(access_token).next())
            {
                try {

                    if(!jetty_server.db.getFullMarks(id).next())
                        return Response.status(Response.Status.NOT_FOUND).entity("404 Not Found").build();


                    ResultSet marks = jetty_server.db.getFullMarks((int) id);
                    ResultSet cars = jetty_server.db.getCarsFromMarks((int) id);

                    JSONArray array = jetty_server.oath.convert(marks);

                    if(cars != null)
                        array.put(JSONArrayToJSONObject(jetty_server.oath.convert(cars), "cars"));

                    json = JSONArrayToJSONObject(array, "mark");

                } catch (Exception ex) {
                    System.out.print("Ошибка");
                }

                if(json != null)
                     return Response.ok(json.toString(), MediaType.APPLICATION_JSON).build();

                return Response.status(Response.Status.NOT_FOUND).entity("404 Not Found").build();

            }else
            {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }catch (Exception ex)
        {
            return Response.serverError().build();
        }
    }


    @POST
    @Path("/marks")
    @Produces("text/plain")
    public Response  setMark(@QueryParam("name") String name,
                             @QueryParam("contry") String contry,
                             @QueryParam("info") String info,
                             @Context HttpServletRequest httpRequest)
    {
        JSONObject json = null;
        String access_token = httpRequest.getHeader("access_token");

        try
        {
            if(jetty_server.db.checkAccessToken(access_token).next())
            {

                int num = jetty_server.db.SetNewMarks(name, contry, info);
                if( num >= 0) {
                    URI uri = new  URI(httpRequest.getContextPath() + "/" + num);
                    return Response.created(uri).build();
                }
                return Response.status(Response.Status.BAD_REQUEST).build();

            }else
            {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }catch (Exception ex)
        {
            return Response.serverError().build();
        }
    }


    @PUT
    @Path("/marks/{id}")
    @Produces("text/plain")
    public Response  updateMark(@PathParam("id") int id,
                                @Context HttpServletRequest httpRequest)
    {
        JSONObject json = null;
        String access_token = httpRequest.getHeader("access_token");

        try
        {
            if(jetty_server.db.checkAccessToken(access_token).next())
            {
                try {

                    Map<String, String> map = jetty_server.oath.queryToMap(httpRequest.getQueryString());
                    Map<String, String> map2 =  new HashMap<String, String>();

                    for(String str: this.marksFields)
                    {
                        if(map.get(str) != null)
                            map2.put(str, map.get(str));
                        else
                            return Response.status(Response.Status.BAD_REQUEST).build();
                    }

                    if(!jetty_server.db.getFullMarks(id).next())
                        return Response.status(Response.Status.NOT_FOUND).entity("404 Not Found").build();

                    if(jetty_server.db.updateMarks(id, map))
                        return Response.ok().build();

                } catch (Exception ex) {
                    System.out.print("Ошибка");
                }

                return Response.notModified().build();
            }else
            {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }catch (Exception ex) {
            return Response.serverError().build();
        }
    }


    @PATCH
    @Path("/marks/{id}")
    @Produces("text/plain")
    public Response  updateMarksSomeFields(@PathParam("id") int id,
                                           @Context HttpServletRequest httpRequest)
    {
        String access_token = httpRequest.getHeader("access_token");

        try
        {
            if(jetty_server.db.checkAccessToken(access_token).next())
            {
                try {

                    Map<String, String> map = jetty_server.oath.queryToMap(httpRequest.getQueryString());
                    Map<String, String> map2 =  new HashMap<String, String>();

                    for(String str: this.marksFields)
                    {
                        if(map.get(str) != null)
                            map2.put(str, map.get(str));
                    }

                    if(!jetty_server.db.getFullMarks(id).next())
                        return Response.status(Response.Status.NOT_FOUND).entity("404 Not Found").build();

                    if(jetty_server.db.updateMarks(id, map2))
                        return Response.ok().build();

                } catch (Exception ex) {
                    System.out.print("Ошибка");
                }

                return Response.serverError().build();
            }else
            {
                return Response.status(Response.Status.FORBIDDEN).type("text/plain").entity("403 Forbidden").build();
            }
        }catch (Exception ex) {
            return Response.serverError().build();
        }
    }


    @DELETE
    @Path("/marks/{id}")
    @Produces("text/plain")
    public Response  deleteMark(@PathParam("id") int id,
                                @Context HttpServletRequest httpRequest)
    {
        String access_token = httpRequest.getHeader("access_token");
        try
        {
            if(jetty_server.db.checkAccessToken(access_token).next())
            {
                try {

                    if(!jetty_server.db.getCarsFromMarks(id).next()) {
                        if (jetty_server.db.deleteMark(id))
                            return Response.status(204).build();
                    }else
                    {
                        return Response.status(409).build();
                    }

                } catch (Exception ex) {
                    System.out.print("Ошибка");
                }
                return Response.status(Response.Status.NOT_FOUND).entity("404 Not Found").build();
               // return Response.status(Response.Status.NOT_MODIFIED).build();
            }else
            {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }catch (Exception ex) {
            return Response.serverError().build();
        }
    }

    //endregion



}
