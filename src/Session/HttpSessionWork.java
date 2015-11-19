package Session;

/**
 * Created by user on 25.10.15.
 */
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class HttpSessionWork extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

            //creating a session
            HttpSession session = request.getSession();
    }


}