package jetty_server;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import db.db_work;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.client.HttpClient;
import Example.*;
import Authorization.*;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.http.HttpMethod;
import db.db_work.*;
import GenerateHtmlFile.*;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletContext;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by user on 23.10.15.
 */
public class jetty_server
{
    public static  Map<String, String> userdata;
    public static  Authorization oath;
    public static db_work db;
    public static GenerateHtml html;

    public static void main(String[] args) throws Exception
    {
        Server server = new Server(5000);

        db = new db_work();
        oath = new Authorization(db);
        html = new GenerateHtml();




        HashSessionIdManager idmanager = new HashSessionIdManager();
        server.setSessionIdManager(idmanager);
        HashSessionManager manager = new HashSessionManager();

    //    SessionHandler sessions = new SessionHandler(manager);

        ServletContextHandler root = new ServletContextHandler(server, "/" , ServletContextHandler.SESSIONS);
       // root.setContextPath("/");
      //  server.setHandler(root);

   /*     ServletHolder holder1 = new ServletHolder(new GetCode(oath, html));
        root.addServlet(holder1, "/oath/autho");

        ServletHolder holder2 = new ServletHolder(new GetAccessToken(oath, db, html));
        root.addServlet(holder2, "/oath/code");
*/
        ServletHolder holder3 = new ServletHolder(new Test());
        root.addServlet(holder3, "/test/{id}");

        ServletHolder holder4 = new ServletHolder(new GetMe(oath, html));
        root.addServlet(holder4, "/me");

        ServletHolder holder5 = new ServletHolder(new GetIndex(oath, html));
        root.addServlet(holder5, "/*");

      //  ServletHolder holder6 = new ServletHolder(new Regis(oath, html));
      //  root.addServlet(holder6, "/regis");

        ServletHolder holder7 = new ServletHolder(new Welcome());
        root.addServlet(holder7, "/wel");

        ServletHolder holder8 = new ServletHolder(new Login(oath));
        root.addServlet(holder8, "/login");

        ServletHolder holder9 = new ServletHolder(new Logout(oath, html));
        root.addServlet(holder9, "/logout");


        // API

        ServletHolder sh = new ServletHolder(ServletContainer.class);
        sh.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");
        sh.setInitParameter("com.sun.jersey.config.property.packages", "rest");//Set the package where the services reside
        sh.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
        root.addServlet(sh, "/sr/*");
      //  ServletContextHandler context = new ServletContextHandler(server, "/api", ServletContextHandler.SESSIONS);




    /*    ContextHandler context1 = new ContextHandler("/oath/autho");
        context1.setHandler(new GetCode(oath));

        ContextHandler context2 = new ContextHandler("/oath/code");
        SessionHandler sessions1 = new SessionHandler(manager);
        context2.setHandler(sessions1);
        sessions1.setHandler(new GetAccessToken(oath, db));

        ContextHandler context3 = new ContextHandler("/oath/accessToken");
        context3.setHandler(new ReadAccessToken(oath));

        ContextHandler context4 = new ContextHandler("/me");
        context4.setHandler(new GetMeClass(oath));

        ContextHandler context5 = new ContextHandler("/");
        context5.setHandler(new GetIndex(oath, html));

        ContextHandler context6 = new ContextHandler("/regis");
        context6.setHandler(new Regis(oath, html));

        ContextHandler context7 = new ContextHandler("/ex");
    //    SessionHandler sessions2 = new SessionHandler(manager);
        context7.setHandler(sessions1);
        sessions1.setHandler(new GetMe());



        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[]{context1, context2, context3, context4, context5, context6, context7});
        server.setHandler(contexts);
*/

      //  ServletContextHandler root1 = new ServletContextHandler(ServletContextHandler.SESSIONS);
     //   root1.setContextPath("/");
       // root1.addServlet(new ServletHolder(new Welcome()), "/*");
      /*  server.setHandler(root1);

        ServletHolder holder1 = new ServletHolder(new Login());
        root1.addServlet(holder1, "/log");

        ServletHolder holder2 = new ServletHolder(new Welcome());
        root1.addServlet(holder2, "/wel");
        */
        /*ServletContextHandler root2 = new ServletContextHandler(contexts, "/log", ServletContextHandler.SESSIONS);
        root2.addServlet(new ServletHolder(new Login()), "/*");
*/


/*
        ServletContextHandler.Context other = new Context(contexts,"/other", ServletContextHandler.Context.SESSIONS);
        other.addServlet("org.mortbay.jetty.example.ManyServletContexts$HelloServlet", "/*");
*/
     //   StatisticsHandler stats = new StatisticsHandler();
     //   contexts.addHandler(stats);

        /*
        Context yetanother =new Context(stats,"/yo",Context.SESSIONS);
        yetanother.addServlet(new ServletHolder(new HelloServlet("YO!")), "/*");

*/

        server.start();
        server.join();


    }
}