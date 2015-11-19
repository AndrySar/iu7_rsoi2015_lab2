package db;

import javax.ws.rs.QueryParam;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by user on 25.10.15.
 */
public class db_work {

    private Connection connection = null;
    public db_work()
    {
        ConnectBD();
    }

    protected void finalize()
    {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(db_work.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void ConnectBD() {
        //URL к базе состоит из протокола:подпротокола://[хоста]:[порта_СУБД]/[БД] и других_сведений
        String url = "jdbc:postgresql://127.0.0.1:5432/rsoi";
        //Имя пользователя БД
        String name = "andry";
        //Пароль
        String password = "andry";
        try {
            //Загружаем драйвер
            Class.forName("org.postgresql.Driver");
            System.out.println("Драйвер подключен");
            //Создаём соединение
            this.connection = DriverManager.getConnection(url, name, password);
            System.out.println("Соединение установлено");

        } catch (Exception ex) {
            //выводим наиболее значимые сообщения
            Logger.getLogger(db_work.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ResultSet EcecuteQuery(String sqlRequest)
    {
        ResultSet result1 = null;
        try {

            Statement statement = null;
            statement = this.connection.createStatement();
            //Выполним запрос
            result1 = statement.executeQuery(sqlRequest);

        }catch (Exception ex){

            Logger.getLogger(db_work.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            return result1;
        }
    }

    public boolean executeUpdateOrInsert(String sqlRequest)
    {
        try {
            Statement statement = null;
            statement = this.connection.createStatement();
            //Выполним запрос
            statement.executeUpdate(sqlRequest);
            return true;
        }catch (Exception ex){

            Logger.getLogger(db_work.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public ResultSet getUsers()
    {
        String sql = "SELECT * FROM users;";
        return this.EcecuteQuery(sql);
    }

    public ResultSet getUserById(Integer id)
    {

        String sql = "SELECT * FROM users where id =" + id + ";";
        return this.EcecuteQuery(sql);
    }

    public ResultSet checkAccessToken(String access_token)
    {
        String sql = "SELECT * FROM keys where access_token ='" + access_token + "';";
        return this.EcecuteQuery(sql);
    }

    public ResultSet getUserByHexId(String hex_id)
    {

        String sql = "SELECT * FROM users where hex_id ='" + hex_id + "';";
        return this.EcecuteQuery(sql);
    }

    public ResultSet getUserLoginPasswd(String login, String passwd)
    {
        String sql = "SELECT * FROM users WHERE login='" + login + "' and passwd='" + passwd + "';";
        return  this.EcecuteQuery(sql);
    }

    public boolean createUser(String  login, String passwd, String name, String address)
    {
        String sql =  "INSERT INTO users(login, passwd, name, address) values('"
                      + login +"', '" + passwd +"', '" + name + "', '" + address + "');";
        int i = 0;
        try {

            Statement statement = this.connection.createStatement();
            i = statement.executeUpdate(sql);

        }catch (Exception ex){

            Logger.getLogger(db_work.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(i > 0)
            return true;
        else
            return false;
    }

    public boolean UpdateUser(Map<String, String> user_data, String hex_id)
    {
        String sql = "UPDATE users SET ";

        for(Object key : user_data.keySet()) {
            Object value = user_data.get(key);
            sql += key + " = '" + value + "' ";
        }

        sql += "where hex_id = " + hex_id + ";";

        executeUpdateOrInsert(sql);
        return true;
    }



    /*
            Работа с таблицей cars
     */
    public ResultSet GetCarFull(int id)
    {
        String sql = "SELECT * FROM cars WHERE id=" + id + ";";

        ResultSet res = EcecuteQuery(sql);
        return res;
    }

    public ResultSet GetAllCars(int limit, int offset)
    {
        String sql = "SELECT id, marks_id, model, mileage, year FROM cars ORDER BY id LIMIT " + limit + " OFFSET " + offset + ";";
        ResultSet res = EcecuteQuery(sql);
        return res;
    }

    public ResultSet GetAllCars()
    {
        String sql = "SELECT id, marks_id, model, mileage, year FROM cars ORDER BY id ;";
        ResultSet res = EcecuteQuery(sql);
        return res;
    }


    // Запросы на получение всех марок автомобиля
    public ResultSet GetAllMarks(int limit, int offset)
    {
        String sql = "SELECT id, name, info FROM marks ORDER BY id LIMIT " + limit + " OFFSET " + offset + ";";
        ResultSet res = EcecuteQuery(sql);
        return res;
    }

    // Запрос на получение конкретной марки автомобиля
    public ResultSet getFullMarks(int id)
    {
        String sql = "SELECT name, contry, info FROM marks WHERE id=" + id + ";";
        ResultSet res = EcecuteQuery(sql);
        return res;
    }

    public ResultSet getCarsFromMarks(int marks_id)
    {
        String sql = "SELECT id, model, year, cost FROM cars WHERE marks_id=" + marks_id + ";";
        ResultSet res = EcecuteQuery(sql);
        return res;
    }



    public int SetNewCars(String model,
                              int marks,
                              int power,
                              int mileage,
                              int year,
                              String color,
                              String drive,
                              String info,
                              int cost)
    {
        String sql = "INSERT INTO cars(model, marks_id, power, mileage, year, color,drive, info, cost) " +
                     "values('" + model + "','"  + marks +"', '" + power + "', '" + mileage + "','" +
                    + year + "', '" + color + "', '" + drive + "', '" + info + "', '" + cost + "');";

       if(!this.executeUpdateOrInsert(sql))
           return -1;

        sql = "SELECT currval('cars_id');";
        ResultSet res = EcecuteQuery(sql);

        try {
            res.next();
            return Integer.parseInt(res.getString("currval"));
        }catch(Exception ex)
        {
            return -1;
        }
    }

    public boolean updateCars(int id, Map<String, String> map)
    {
        String sql = "UPDATE cars SET ";
        String point = "";

        for(Object key : map.keySet()) {
            Object value = map.get(key);
            if(value != null)
            sql +=  point + key + " = '" + value + "' ";

            point = ", ";
        }

        sql += "where id = " + id + ";";

        return this.executeUpdateOrInsert(sql);
    }

    public boolean deleteCar(int id)
    {
        try {
            if (this.GetCarFull(id).next()) {
                String sql = "DELETE FROM cars WHERE id = " + id;
                return this.executeUpdateOrInsert(sql);
            }
        }catch (Exception ex)
        {

        }
        return false;
    }



    /*
         Работа с таблицей marks
     */
    public int SetNewMarks(String name, String contry, String info)
    {
        String sql = "INSERT INTO marks(name, contry, info) " +
                "values('" + name + "', '"  + contry +"', '" + info + "');";
        this.executeUpdateOrInsert(sql);
        sql = "SELECT currval('mark_id');";
        ResultSet res = EcecuteQuery(sql);

        try {
            res.next();
            return Integer.parseInt(res.getString("currval"));
        }catch(Exception ex)
        {
            return -1;
        }
    }

    public boolean updateMarks(int id, Map<String, String> map)
    {
        String sql = "UPDATE marks SET ";
        String point = "";

        for(Object key : map.keySet()) {
            Object value = map.get(key);
            if(value != null)
                sql +=  point + key + " = '" + value + "' ";

            point = ", ";
        }

        sql += "where id = " + id + ";";

        return this.executeUpdateOrInsert(sql);
    }

    public boolean deleteMark(int id)
    {
        try {
           // if (this.getFullMarks(id).next()) {

             //   ResultSet res = this.getCarsFromMarks(id);

             //   if(res.next())
            //        throw new
               /* while(res.next())
                {
                    this.deleteCar((int)res.getObject("id"));
                }
                */
                String sql = "DELETE FROM marks WHERE id = " + id;
                return this.executeUpdateOrInsert(sql);
         //   }
        }catch (Exception ex)
        {

        }
        return false;
    }

    public int countRecord(String namedb)
    {
        String sql = "SELECT COUNT(*) FROM " + namedb + ";";
         ResultSet res = this.EcecuteQuery(sql);
        try{
        if(res.next()) {
            return Integer.parseInt(res.getString("count"));
        }
        }catch (Exception ex)
        {
        }
        return 0;
    }


    public ResultSet chaeckClientId(String client_id)
    {
        String sql = "SELECT * FROM applicationkeys WHERE client_id='" + client_id + "';";
        return this.EcecuteQuery(sql);
    }

    public ResultSet chaeckSecretClient(String secret_client)
    {
        String sql = "SELECT * FROM applicationkeys WHERE secret_client='" + secret_client + "';";
        return this.EcecuteQuery(sql);
    }

    public ResultSet getKeyRecord(String login)
    {
        String sql = "SELECT * FROM keys WHERE login='" + login + "';";
        return this.EcecuteQuery(sql);
    }

    public String setKeyRecord(String login, String access_token)
    {
        Calendar calendar = Calendar.getInstance();
        java.sql.Date currentJavaDateObject = new java.sql.Date(calendar.getTime().getTime());

        String sql = "INSERT INTO keys(login, access_token, create_date) " +
                "values('" + login + "', '"  + access_token +"', '" + currentJavaDateObject + "');";

        if(this.executeUpdateOrInsert(sql))
            return access_token;

        return "error request";
    }

    public ResultSet getAccessToken(String login)
    {
        String sql = "SELECT * FROM keys WHERE login='" + login + "';";
        return this.EcecuteQuery(sql);
    }

   /* public ResultSet getInfo(String access_token)
    {
        String sql = "SELECT * FROM users, keys"
    }*/

    public boolean setCodeState(String code, int state, String login)
    {
        Calendar calendar = Calendar.getInstance();
        java.sql.Date currentJavaDateObject = new java.sql.Date(calendar.getTime().getTime());

        String sql = "INSERT INTO code(current_code, state, date_create, login) " +
                "values('" + code + "', '"  + state +"', '" + currentJavaDateObject + "', '" + login + "');";

        return this.executeUpdateOrInsert(sql);
    }


    public ResultSet checkCodeState(String code, int state)
    {
        String sql = "SELECT * FROM code WHERE current_code='" + code + "' and state=" + state + ";";
        return this.EcecuteQuery(sql);
    }


 }
