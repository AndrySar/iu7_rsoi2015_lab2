package Authorization;

import com.sun.net.httpserver.HttpExchange;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by user on 10.10.15.
 */
public class Authorization {
    public void GetCode(HttpExchange exc)
    {
        String result = "";
        try {
                String httpURL = "https://github.com/login/oauth/authorize?client_id=32ab75ec177d1d73da83&redirect_uri=http://localhost:5000/oath/code";
                exc.getResponseHeaders().add("Location", httpURL);


        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    public String GetAccess_Tocken(String code)
    {
        String inputLine = "";
        String str = "";
        try {
            String httpURL = "https://github.com/login/oauth/access_token?" +
                    "client_id=32ab75ec177d1d73da83" +
                    "&client_secret=e72e22e9d78b6fbbdf80572132aa9ededceb2bcf" +
                    "&code=" + code;

            URL currentURL = new URL(httpURL);
            HttpsURLConnection con = (HttpsURLConnection) currentURL.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            InputStream ins = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(ins);
            BufferedReader in = new BufferedReader(isr);



            while ((inputLine = in.readLine()) != null) {
                // System.out.println(inputLine);
                str += inputLine;
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        return str;
    }

    public String GetMe(String Access_Token)
    {
        String inputLine = "";
        String str = "";
        try {
            String httpURL = "https://api.github.com/user?access_token=" + Access_Token;

            URL currentURL = new URL(httpURL);
            HttpsURLConnection con = (HttpsURLConnection) currentURL.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            InputStream ins = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(ins);
            BufferedReader in = new BufferedReader(isr);


            while ((inputLine = in.readLine()) != null) {
                str += inputLine;
            }
        } catch (Exception e) {
            str = e.getMessage();
        }
        return str;
    }

    public String State()
    {
        String inputLine = "";
        String str = "";
        try {
            String httpURL = "https://api.github.com/user";

            URL currentURL = new URL(httpURL);
            HttpsURLConnection con = (HttpsURLConnection) currentURL.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            InputStream ins = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(ins);
            BufferedReader in = new BufferedReader(isr);


            while ((inputLine = in.readLine()) != null) {
                str += inputLine;
            }
        } catch (Exception e) {
            str = e.getMessage();
           // System.out.print(e.getMessage());
        }
        return str;
    }

}
