package comAFC.BackendAFC.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;

@RestController
public class MainController {
    private static final String USERNAME = "root";
    private static final String PASSWORD = "N11233455";
    private static final String URL = "jdbc:mysql://localhost:3306/Data_test";

    @GetMapping("/api/main")
    @CrossOrigin (origins = "http://127.0.0.1:5501")
    public String mainLisner(){
        String newText = "{ \"USERS\" : [ ";
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME,PASSWORD);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from Users");

            while (resultSet.next()) {
//resultSet.getString("user_name")
//                    System.out.println(resultSet.getString("user_name"));
                if(newText.equals("{ \"USERS\" : [ ")){

                } else {
                    newText = newText + ",";
                }
                    newText= newText+ "{\"user\": \""+resultSet.getString("user_name")+"\"}";
            }
            newText = newText + "] }";

        } catch (SQLException e) {
            System.out.println(("lOl") + e.getMessage());
            return "Hello World";
        }
        return newText;
    }

    @GetMapping("/api/user")
    @CrossOrigin (origins = "http://127.0.0.1:5501")
    public String GetUser(@RequestParam String user, String password){
        try{
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from Users");
            while (resultSet.next()) {
                if((resultSet.getString("user_name").equals(user) && resultSet.getString("user_password").equals(password))){
                    return "{ \"User\" : [ {\"Connection\": \"True\"}, {\"user_name\": \""+user+"\"} , {\"user_email\": \""+resultSet.getString("user_email")+"\"}] }";
                }
            }
        } catch (Exception e) {
            return "{ \"User\" : [ {\"Connection\": \"False\"}] }";
        }
        return "{ \"User\" : [ {\"Connection\": \"False\"}] }";
    }

    @GetMapping("/api/add")
    @CrossOrigin (origins = "http://127.0.0.1:5501")
    public String AdminAddFood(@RequestParam String admin, String password, String food){

      try {
          Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
          String sql1 =("create table "+food+" (\n" +
                  "  src varchar(128),\n" +
                  "  info varchar(128),\n" +
                  "  price int ,\n" +
                  "  name varchar(64)\n" +
                  ");" );

          String sql2 = "INSERT INTO food_name(name) VALUE (?);";
          if(admin.equals("admin") && password.equals("12345")){
              PreparedStatement statement1 = connection.prepareStatement(sql1);
              PreparedStatement statement2 = connection.prepareStatement(sql2);
              statement1.executeUpdate();
              statement2.setString(1, food);
              statement2.executeUpdate();
//               String sql =("INSERT INTO food_name(name) VALUE (\""+food+"\");" + "create table "+food+" ( src varchar(128),info varchar(128),price int ,name varchar(64));");
//              resultSet = statement.executeUpdate("create table "+food+" ( src varchar(128),info varchar(128),price int ,name varchar(64));");
          } else {
              return "{ \"connect\" : \"False\"}";
          }
      } catch (Exception e) {
          return "{ \"connect\" : \"Error\"}" ;
      }
      return "{ \"connect\" : \"True\"}";
    }

    @GetMapping("/api/addstring")
    @CrossOrigin (origins = "http://127.0.0.1:5501")
    public String AdminAddString(String src, String info, Long price, String name, String table, String admin, String password){
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "INSERT INTO "+table+"(src, info,price,name) VALUE (?,?,?,?);";
            PreparedStatement statement1 = connection.prepareStatement(sql);
            if(admin.equals("admin") && password.equals("12345")){
                statement1.setString(1, src);
                statement1.setString(2, info);
                statement1.setLong(3, price);
                statement1.setString(4, name);
                statement1.executeUpdate();
            } else {
                return "{ \"connect\" : \"False\"}";
            }
        } catch (SQLException e) {
            return "{ \"connect\" : \"Error\"}" ;
        }
        return "{ \"connect\" : \"True\"}";
    }
    @GetMapping("/api/getfood")
    @CrossOrigin (origins = "http://127.0.0.1:5501")
    public String GetFood(){
        String text = "{";
        try{
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from food_name");
            Statement statement1 = connection.createStatement();
            while (resultSet.next()) {
                if(text.equals("{")){
                }else{
                    text = text + ",";
                }
                text = text + " \""+resultSet.getString("name")+"\" : [ ";
                String text1 = text;
                ResultSet resultSet1 = statement1.executeQuery("select * from "+ resultSet.getString("name"));
               while(resultSet1.next()){
                   if(text.equals(text1)){
                   } else {
                       text = text + ",";
                   }
                   text = text + "{\"src\": \""+resultSet1.getString("src")+":\",\"info\": \""+resultSet1.getString("info")+"\", \"price\": "+resultSet1.getString("price")+", \"name\":\""+resultSet1.getString("name")+"\"}";
               }
                text = text + "]";
            }
        } catch (SQLException e) {
            return (("error") + e.getMessage())+ text;
        }
text = text + "}";
        return text;
    }

    @GetMapping("/api/delfood")
    @CrossOrigin (origins = "http://127.0.0.1:5501")
    public String delFood(String food, String table, String admin, String password ){
        //DELETE FROM burger WHERE name = 'burger1';
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            if (admin.equals("admin") && password.equals("12345")) {
                PreparedStatement statement = connection.prepareStatement("DELETE FROM "+table+" WHERE name = '"+food + "' ");
                statement.executeUpdate();
                return "{ \"connect\" : \"True\"}";
            } else {
                return "{ \"connect\" : \"False\"}";
            }
        } catch (Exception e) {
            return "{ \"connect\" : \"error\"}";
        }
    }

    @GetMapping("/api/deltable")
    @CrossOrigin (origins = "http://127.0.0.1:5501")
    public String delTable(String table, String admin, String password ){
        try {
            if (admin.equals("admin") && password.equals("12345")) {
                Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                PreparedStatement statement = connection.prepareStatement("DELETE FROM food_name WHERE name = '"+table + "' ");
                statement.executeUpdate();
                PreparedStatement statement1 = connection.prepareStatement("DROP table " +table);
                statement1.executeUpdate();

                return "{ \"connect\" : \"True\"}";
            } else {
                return "{ \"connect\" : \"False\"}";
            }
        } catch (Exception e) {
            return "{ \"connect\" : \"False\"}";
        }


    }
    @GetMapping("/api/reg")
    @CrossOrigin (origins = "http://127.0.0.1:5501")
    public String reg(String name,  String password , String email){
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "INSERT INTO Users(user_name, user_password,user_email) VALUE (?,?,?);";
            PreparedStatement statement1 = connection.prepareStatement(sql);
            statement1.setString(1, name);
            statement1.setString(2, password);
            statement1.setString(3, email);
            statement1.executeUpdate();
            return "{ \"connect\" : \"True\"}";
        } catch (Exception e) {
            return "{ \"connect\" : \"False\"}";
        }
    }
}
