package Example;

/**
 * Created by user on 09.11.15.
 */
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Authorization.Authorization;
import GenerateHtmlFile.GenerateHtml;
import jetty_server.jetty_server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class Logout extends HttpServlet
{
    private Authorization oath;
    private GenerateHtml gHtml;
    public Logout(Authorization param, GenerateHtml gh)
    {
        this.oath = param;
        this.gHtml = gh;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response ) throws IOException, ServletException
    {
        HttpSession session = request.getSession(false);
        if(session != null) {

            session.invalidate();

        }
        response.sendRedirect("/");
    }

}