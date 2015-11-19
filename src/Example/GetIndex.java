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

public class GetIndex extends HttpServlet
{
    private Authorization oath;
    private GenerateHtml gHtml;

    public GetIndex(Authorization param, GenerateHtml gh)
    {
        this.oath = param;
        this.gHtml = gh;
    }

    public void doGet(HttpServletRequest request,
                       HttpServletResponse response ) throws IOException, ServletException
    {


        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        String message = "Не авторизирован";

        HttpSession session = request.getSession(false);
        String resp = "";
        if(session != null) {
            message = session.getAttribute("user").toString();
        }


        PrintWriter out = response.getWriter();
        out.println(this.gHtml.GetIndex(message));
        out.close();
      //  baseRequest.setHandled(true);
    }


    public void doPost(HttpServletRequest request,
                        HttpServletResponse response) throws IOException,
            ServletException
    {
        response.setContentType("text/html; charset=utf-8");

        String message = "Не авторизирован";
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();
        out.println(this.gHtml.GetIndex(message));
        out.close();
    }
}
