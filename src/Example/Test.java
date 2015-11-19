package Example;

/**
 * Created by user on 24.10.15.
 */
import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.client.HttpClient;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import Authorization.*;
import javax.ws.rs.Path;


public class Test extends HttpServlet
{
    private String num;
    @Path("/{id}")
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
    {
        String str = request.getContextPath();
    }
}

