package Example; /**
 * Created by user on 23.10.15.
 */
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class Example extends AbstractHandler
{
    final String greeting;
    final String body;

    public Example()
    {
        this("Hello World");
    }

    public Example( String greeting )
    {
        this(greeting, null);
    }

    public Example( String greeting, String body )
    {
        this.greeting = greeting;
        this.body = body;
    }

    public void handle( String target,
                        Request baseRequest,
                        HttpServletRequest request,
                        HttpServletResponse response ) throws IOException,
            ServletException
    {
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        HttpSession session = request.getSession();
        String user = (String)session.getAttribute("user");

        PrintWriter out = response.getWriter();


        out.println("<h1>" + user + "</h1>");
        if (body != null)
        {
            out.println(body);
        }

        baseRequest.setHandled(true);
    }
}
