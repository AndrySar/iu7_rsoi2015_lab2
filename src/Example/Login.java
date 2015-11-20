package Example;

/**
 * Created by user on 27.10.15.
 */
import Authorization.Authorization;

import java.io.*;
import java.util.Date;
import javax.servlet.*;
import javax.servlet.http.*;


public class Login extends HttpServlet {

    private Authorization oath;
    public Login(Authorization param)
    { this.oath = param;            }


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        response.sendRedirect(this.oath.GetURIforCode());

      //  boolean create = "true".equals(request.getParameter("create"));

      /*  HttpSession session = request.getSession();
        if (session.isNew()) {
            session.setAttribute("created", new Date());
            session.setAttribute("user", "Andry");
        }

        PrintWriter pw = new PrintWriter(response.getOutputStream());
     //   pw.println("Create = " + creat);
        if (session == null) {
            pw.println("no session");
        } else {
            pw.println("Session = " + session.getId());
            pw.println("Created = " + session.getAttribute("created"));
        }

        pw.flush();
        */

 //       HttpSession session = request.getSession(true);
 //       session.setAttribute("user", "Andry");

      //  response.setContentType("text/html;charset=UTF-8");
       // response.setStatus(HttpServletResponse.SC_OK);
      //  PrintWriter out = response.getWriter();
      //  request.getRequestDispatcher("localhost:5000/wel").forward(request, response);
 //       getServletContext().getNamedDispatcher("/wel").forward(request, response);
      //  RequestDispatcher dispatche  = request.getRequestDispatcher("/wel");
     //   dispatche.forward(request, response);
    }
}
