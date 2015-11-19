package Example;

import GenerateHtmlFile.GenerateHtml;
import db.db_work;
import db.db_work.*;

/**
 * Created by user on 24.10.15.
 */
import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.client.api.ContentProvider;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpRequest;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.handler.AbstractHandler;
import Authorization.*;
import jetty_server.*;

public class GetAccessToken extends HttpServlet
{
    private Authorization oath;
    private db_work db;
    private GenerateHtml gHtml;

    public GetAccessToken(Authorization param, db_work db_con, GenerateHtml gh)
    {
        this.oath = param;
        this.db = db_con;
        this.gHtml = gh;
    }

    public void doGet( HttpServletRequest request,
                        HttpServletResponse response ) throws IOException, ServletException
    {
        response.setContentType("text/html; charset=utf-8");

        Map<String, String> map = Authorization.queryToMap(request.getQueryString());

        String result_request = "";

        if(map.get("code") != null)
        {

                try {

                    URL currentURL = new URL(oath.GetAccessTocken(map.get("code")));
                    HttpsURLConnection con = (HttpsURLConnection) currentURL.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                    InputStream ins = con.getInputStream();
                    InputStreamReader isr = new InputStreamReader(ins);
                    BufferedReader in = new BufferedReader(isr);


                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        result_request += inputLine;
                    }


                    String access_token = Authorization.queryToMap(result_request).get("access_token");

                    if(access_token != null && jetty_server.userdata != null)
                    {
                     /*   if(Authorization.Registration(access_token, jetty_server.userdata, db))
                        {
                            HttpSession session = request.getSession(true);
                            session.setAttribute("user", jetty_server.userdata.get("name"));
                            session.setAttribute("access_token", access_token);

                            response.setStatus(HttpServletResponse.SC_CREATED);
                            PrintWriter out = response.getWriter();
                            out.println(gHtml.GetCurrentUser(jetty_server.userdata.get("name")));
                            out.close();
                        }else
                        {
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            PrintWriter out = response.getWriter();
                            out.println(gHtml.GetContent("Ошибка регистрации!"));
                            out.close();
                        }
                        */

                    }else if(access_token != null)
                    {
                        String name;
                        if((name = Authorization.Login(access_token, db)) != null)
                        {
                            HttpSession session = request.getSession(true);
                            session.setAttribute("user",  name );
                            session.setAttribute("access_token", access_token);

                            response.setStatus(HttpServletResponse.SC_OK);
                            PrintWriter out = response.getWriter();
                            out.println(gHtml.GetCurrentUser(name));
                            out.close();
                        }else
                        {
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            PrintWriter out = response.getWriter();
                            out.println(gHtml.GetContent("Ошибка авторизации"));
                            out.close();
                        }
                    }else {

                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        PrintWriter out = response.getWriter();
                        out.println(gHtml.GetContent("Ошибка регистрации!"));
                        out.close();
                    }

                } catch (Exception e) {

                    response.setContentType("text/html; charset=utf-8");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    PrintWriter out = response.getWriter();
                    out.println(gHtml.GetContent("<h1>" + e.getMessage() + "</h1>"));
                    out.close();
                }finally {
                    jetty_server.userdata = null;
                }

        }else
        {
            response.setContentType("text/html; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.println(gHtml.GetContent("h1>" + "Error" + "</h1>"));
            out.close();
        }


        //PrintWriter out = response.getWriter();

      //  out.println("<h1>" + "GetCode" + "</h1>");

       // response.sendRedirect(this.oath.GetURIforCode());
    }
}
