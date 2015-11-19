package Authorization;

import Example.GetMe;
import com.sun.net.httpserver.HttpExchange;
import db.db_work;
import jetty_server.jetty_server;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.security.*;

/**
 * Created by user on 10.10.15.
 */
public class Authorization {

    private db_work db;

    public Authorization(db_work conn)
    {
        this.db = conn;
    }

    public String GetURIforCode()
    {
        String httpURL = "";
        try {
                httpURL = "https://github.com/login/oauth/authorize?client_id=32ab75ec177d1d73da83&redirect_uri=http://localhost:5000/oath/code";

        } catch (Exception e) {
            System.out.print(e.getMessage());
        }finally {
            return httpURL;
        }
    }

    public String GetAccessTocken(String code)
    {
        String httpURL = "";
        try {
             httpURL = "https://github.com/login/oauth/access_token?" +
                    "client_id=32ab75ec177d1d73da83" +
                    "&client_secret=e72e22e9d78b6fbbdf80572132aa9ededceb2bcf" +
                    "&code=" + code +
                    "&redirect_uri=http://localhost:5000/oath/code";

        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        return httpURL;
    }

    public static String GetMe(String Access_Token)
    {
        return "https://api.github.com/user?access_token=" + Access_Token;
    }

    public String State()
    {
        String inputLine = "";
        String str = "";
        try {
            String httpURL = "https://github.com/status/";

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
            System.out.print(e.getMessage());
        }
        return str;
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

    public static String Send_Request(String target_url)
    {
        String result_request = "";
        try {

            URL currentURL = new URL(target_url);
            HttpsURLConnection con = (HttpsURLConnection) currentURL.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            InputStream ins = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(ins);
            BufferedReader in = new BufferedReader(isr);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                result_request += inputLine;
            }

        } catch (Exception e) {
            result_request = e.getMessage();
        }finally {
            return result_request;
        }
    }

    public static String convertTomd5(String message)
    {
        try
        {
            byte[] bytesOfMessage = message.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(bytesOfMessage);
            message = thedigest.toString();

            return message;
        }catch (Exception ex) {
            return "";
        }
    }

    public boolean checkUser(String login, String passwd)
    {
        try {
                if (this.db.getUserLoginPasswd(login, hashString(passwd, "MD5")).next())
                    return true;
        }catch (Exception ex)
        {
        }
        return false;
    }

    public static String Login(String access_token, db_work db)
    {
        JSONObject jObject  = new JSONObject(Authorization.Send_Request(GetMe(access_token))); // json
        String id_user = jObject.get("id").toString();
        String hex_id = hashString(id_user, "MD5");

        ResultSet result = db.getUserByHexId(hex_id);
        String name = null;

        try {

            if(result.next())
            {
                String ac = result.getObject("access_token").toString();

                if (!ac.equals(access_token)) {
                    Map<String, String> user_data = new HashMap<String, String>();
                    user_data.put("access_token", access_token);

                    db.UpdateUser(user_data, hex_id);
                }

                name = result.getObject("name").toString();
            }

        } catch (Exception ex) {

        }

        return name;
    }

    public boolean Registration(Map<String, String> userData)
    {
        return this.db.createUser(userData.get("login"),
                                  hashString(userData.get("passwd"), "MD5"),
                                  userData.get("name"),
                                  userData.get("address"));
    }


    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    public static String hashString(String message, String algorithm)
    {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hashedBytes = digest.digest(message.getBytes("UTF-8"));

            return convertByteArrayToHexString(hashedBytes);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {

        }
        return "";
    }

    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }


    public static JSONArray convert( ResultSet rs ) throws SQLException, JSONException {

        JSONArray json = new JSONArray();

        if (rs != null)
        {
            ResultSetMetaData rsmd = rs.getMetaData();

            while (rs.next()) {
                int numColumns = rsmd.getColumnCount();
                JSONObject obj = new JSONObject();

                for (int i = 1; i < numColumns + 1; i++) {
                    String column_name = rsmd.getColumnName(i);

                    if (rsmd.getColumnType(i) == java.sql.Types.ARRAY) {
                        obj.put(column_name, rs.getArray(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.BIGINT) {
                        obj.put(column_name, rs.getInt(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.BOOLEAN) {
                        obj.put(column_name, rs.getBoolean(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.BLOB) {
                        obj.put(column_name, rs.getBlob(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.DOUBLE) {
                        obj.put(column_name, rs.getDouble(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.FLOAT) {
                        obj.put(column_name, rs.getFloat(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.INTEGER) {
                        obj.put(column_name, rs.getInt(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.NVARCHAR) {
                        obj.put(column_name, rs.getNString(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.VARCHAR) {
                        obj.put(column_name, rs.getString(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.TINYINT) {
                        obj.put(column_name, rs.getInt(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.SMALLINT) {
                        obj.put(column_name, rs.getInt(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.DATE) {
                        obj.put(column_name, rs.getDate(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.TIMESTAMP) {
                        obj.put(column_name, rs.getTimestamp(column_name));
                    } else {
                        obj.put(column_name, rs.getObject(column_name));
                    }
                }

                json.put(obj);
            }


        }
        return  json;
    }


    public Map<String, String> cheakFields(String query, String[] fields)
    {
        Map<String, String> map1 = jetty_server.oath.queryToMap(query);
        Map<String, String> map2 =  new HashMap<String, String>();

        for(String str: fields)
        {
            if(map1.get(str) != null)
                map2.put(str, map1.get(str));
            else
                return null;
        }
        return map2;
    }

    public boolean checkClientId(String client_id)
    {
        try{
            if(this.db.chaeckClientId(client_id).next())
                return true;
        }catch (Exception ex)
        {

        }
        return false;
    }

    public boolean checkSecretClient(String secret_client)
    {
        try{
            if(this.db.chaeckSecretClient(secret_client).next())
                return true;
        }catch (Exception ex)
        {

        }
        return false;
    }

    public Map<String, String> workCodeSate(String login)
    {
        Random rand = new Random();

        try
        {
            String code = hashString(String.valueOf(rand.nextLong()), "MD5");
            int state = Math.abs(rand.nextInt());
            if(jetty_server.db.setCodeState(code, state, login))
            {
                Map<String, String> map = new HashMap<String, String>();
                map.put("code", code);
                map.put("state", Integer.toString(state));
                return map;
            }
        }catch (Exception ex)
        {

        }
        return null;
    }




  /*  public String workCodeAccessToken(String login)
    {
        Random rand = new Random();
        try {
            ResultSet res = jetty_server.db.getKeyRecord(login);
                if (res.next())
                {
                    return res.getString("code");
                }else
                {
                    return jetty_server.db.setKeyRecord(login,
                            hashString(String.valueOf(rand.nextLong()), "MD5"),
                            hashString(String.valueOf(rand.nextLong()), "MD5") + hashString(String.valueOf(rand.nextLong()), "MD5"));
                }
        }catch (Exception ex)
        {

        }
        return null;
    }
    */

    public String getAccessToken(String code, int state)
    {
        Random rand = new Random();
        try {

            ResultSet res = jetty_server.db.checkCodeState(code, state);

            if(res.next())
            {
                ResultSet res_at = jetty_server.db.getAccessToken(res.getString("login"));

                if(res_at.next())
                {
                    return res_at.getString("access_token");
                }else
                {
                    return jetty_server.db.setKeyRecord(res.getString("login"),
                                    hashString(String.valueOf(rand.nextLong()), "MD5") +
                                    hashString(String.valueOf(rand.nextLong()), "MD5"));
                }
            }

        }catch (Exception ex)
        {

        }
        return null;
    }

}
