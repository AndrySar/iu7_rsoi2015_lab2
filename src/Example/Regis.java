package Example;

/**
 * Created by user on 25.10.15.
 */
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import Authorization.*;
import GenerateHtmlFile.*;

public class Regis extends HttpServlet
{
    private Authorization oath;
    private GenerateHtml gHtml;

    public Regis(Authorization param, GenerateHtml gh)
    {
        this.oath = param;
        this.gHtml = gh;
    }

    public void doGet( HttpServletRequest request,
                        HttpServletResponse response ) throws IOException,
            ServletException
    {
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();
        out.println(this.gHtml.GetRegis());
        out.close();
    }
}