package Example;

/**
 * Created by user on 27.10.15.
 */
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Welcome extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

      /*  HttpSession session = request.getSession();
        String user = (String)session.getAttribute("user");

        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        out.println("Hello " + user );
        */

        HttpSession session = request.getSession(false);


        PrintWriter pw = new PrintWriter(response.getOutputStream());
        pw.println("Create ");
        if (session == null) {
            pw.println("no session");
        } else {
            pw.println("Session = " + session.getId());
           // pw.println("Created = " + session.getAttribute("created"));
            pw.println("userName = " + session.getAttribute("user"));
        }

        pw.flush();
    }
}