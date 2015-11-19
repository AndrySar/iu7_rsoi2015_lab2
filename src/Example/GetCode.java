package Example;

/**
 * Created by user on 24.10.15.
 */

import GenerateHtmlFile.GenerateHtml;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import Authorization.*;
import jetty_server.*;

public class GetCode extends HttpServlet
{
    private Authorization oath;
    private GenerateHtml gHtml;
    public GetCode(Authorization param, GenerateHtml gh)
    {
        this.oath = param;
        gHtml = gh;
    }

    public void doGet( HttpServletRequest request,
                       HttpServletResponse response  ) throws IOException, ServletException
    {
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        try {
            jetty_server.userdata = Authorization.queryToMap(request.getQueryString());
        }catch (NullPointerException ex)
        {
            PrintWriter out = response.getWriter();
            out.println(gHtml.GetContent("<h1>" + "Content is available" + "</h1>"));
        }



        response.sendRedirect(this.oath.GetURIforCode());

    }
}
