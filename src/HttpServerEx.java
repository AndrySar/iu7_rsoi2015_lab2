/**
 * Created by user on 09.10.15.
 */
import java.util.Map;
import java.util.HashMap;
import java.io.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import org.json.*;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import GenerateHtmlFile.GenerateHtml;
import Authorization.Authorization;

import com.sun.net.httpserver.*;
import com.sun.xml.internal.messaging.saaj.packaging.mime.Header;
import org.omg.CORBA.portable.*;

public class HttpServerEx  {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create().create(new InetSocketAddress(5000), 10);
        HttpServerEx.generateHtml = new GenerateHtml();
        HttpServerEx.oath = new Authorization();

        server.createContext("/", new Test1());
        server.createContext("/user", new Test2());
        server.createContext("/file", new GetIndex());
        server.createContext("/status", new Status());
        server.createContext("/oath/autho", new AuthorizationTask());
        server.createContext("/oath/code", new Code());
        server.createContext("/oath/access_token", new Access_token());

        server.start();
        System.out.println("Server started\nPress any key to stop...");
        System.in.read();
        server.stop(0);
        System.out.println("Server stoped");

    }

    private static GenerateHtml generateHtml;
    private static Authorization oath;
    public static String code;

    static class Test1 implements HttpHandler {
        public void handle(HttpExchange exc) throws IOException {
            exc.sendResponseHeaders(200, 0);
            PrintWriter out = new PrintWriter(exc.getResponseBody());
            out.println("Hello moto");
            out.close();
            exc.close();
        }
    }


    static class Test2 implements HttpHandler {
        public void handle(HttpExchange exc) throws IOException {
            exc.sendResponseHeaders(200, 0);
            PrintWriter out = new PrintWriter(exc.getResponseBody());
            out.println("Hello moto test2");
            out.close();
            exc.close();
        }
    }

    static class GetIndex implements HttpHandler {
        public void handle(HttpExchange exc) throws IOException {
            //  Headers h = exc.getRequestHeaders();
            //   h.add("Content-Type", "text/html");

            File file = new File("/home/user/httpserver/test.html");
            byte[] bytearray = new byte[(int) file.length()];
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(bytearray, 0, (int) file.length());



            exc.sendResponseHeaders(200, file.length());

            OutputStream os = exc.getResponseBody();
            os.write(bytearray);
            os.close();
            exc.close();
        }

    }


    static class Status implements HttpHandler {
        public void handle(HttpExchange exc) throws IOException {
            String respons = HttpServerEx.oath.GetMe(HttpServerEx.token);


            exc.sendResponseHeaders(200, 0);
            OutputStream os = exc.getResponseBody();
            os.write(respons.getBytes());
            os.close();


        }

        private GenerateHtml ghg;

    }


    static class AuthorizationTask implements HttpHandler {
        public void handle(HttpExchange exc) throws IOException {
            HttpServerEx.oath.GetCode(exc); // запрос параметра code
            exc.sendResponseHeaders(302, 0);
            exc.close();
        }

    }

    private static String token;

    static class Code implements HttpHandler {
        public void handle(HttpExchange exc) throws IOException {
            Map<String, String> result = queryToMap(exc.getRequestURI().getQuery());

            if (HttpServerEx.token == null){
                String cd = result.get("code");
               // result.clear();

                Map<String, String> access_token = queryToMap(HttpServerEx.oath.GetAccess_Tocken(cd));
                HttpServerEx.token = access_token.get("access_token");

                exc.close();
            }else {



                String respons = HttpServerEx.oath.GetMe(HttpServerEx.token);

                JSONObject obj = new JSONObject(respons);
                String Username = obj.getString("login");
                String html = HttpServerEx.generateHtml.GetCurrentUser(Username);

                exc.sendResponseHeaders(200, 0);
                OutputStream os = exc.getResponseBody();
                os.write(html.getBytes());
                os.close();


            }
            exc.close();
        }
    }

    static class Access_token implements HttpHandler {
        public void handle(HttpExchange exc) throws IOException {

            exc.close();
        }
    }



    public static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            } else {
                result.put(pair[0], "");
            }
        }
        return result;
    }

}


