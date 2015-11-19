package Example;

/**
 * Created by user on 26.10.15.
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

public class GetMe extends HttpServlet
{
    private Authorization oath;
    private GenerateHtml gHtml;
    public GetMe(Authorization param, GenerateHtml gh)
    {
        this.oath = param;
        this.gHtml = gh;
    }

    public void doGet(HttpServletRequest request,
                       HttpServletResponse response ) throws IOException, ServletException
    {
        HttpSession session = request.getSession(false);
        String resp = "";
        if(session != null) {

            String req = oath.GetMe(session.getAttribute("access_token").toString());
            resp = Authorization.Send_Request(req);
        }else
        {
            String req = oath.GetMe("");
            resp = Authorization.Send_Request(req);
        }

        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        out.println(gHtml.GetContent(resp));
        out.close();

    }

}
