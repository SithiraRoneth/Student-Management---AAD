/* Created By Sithira Roneth
 * Date :6/29/24
 * Time :14:18
 * Project Name :StudentManagment
 * */
package lk.ijse.studentmanagment.Controller;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.studentmanagment.Dto.StudentDto;
import lk.ijse.studentmanagment.Util.UtilProcess;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet(urlPatterns = "/student")
public class StudentController extends HttpServlet {
    Connection connection;
    static String SAVE_STUDENT = "INSERT INTO student (id,name,city,age) VALUES (?,?,?,?)";
    static String UPDATE_STUDENT = "UPDATE student SET name = ? , city = ? , age = ? WHERE id = ?";
    static String DELETE_STUDENT = "DELETE FROM student WHERE id = ? ";
    static String GET_STUDENT = "SELECT * FROM student";

    @Override
    public void init() throws ServletException {
        try {
            var driver = getServletContext().getInitParameter("driver-class");
            var dbUrl = getServletContext().getInitParameter("dbURL");
            var userName = getServletContext().getInitParameter("dbUserName");
            var password = getServletContext().getInitParameter("dbPassword");
            Class.forName(driver);
            this.connection =  DriverManager.getConnection(dbUrl,userName,password);
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // ToDo: get details
        try(var writer = resp.getWriter()){
            var ps = connection.prepareStatement(GET_STUDENT);

            ResultSet resultSet = ps.executeQuery();
            List<StudentDto> studentDtoList = new ArrayList<>();

            while (resultSet.next()) {
                StudentDto studentDto = new StudentDto();
                studentDto.setId(resultSet.getString("id"));
                studentDto.setName(resultSet.getString("name"));
                studentDto.setCity(resultSet.getString("city"));
                studentDto.setAge(resultSet.getString("age"));
                studentDtoList.add(studentDto);
            }

            Jsonb jsonb = JsonbBuilder.create();
            String Student = jsonb.toJson(studentDtoList);

            System.out.println("Students : " + Student);
            writer.write(Student);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Todo: Save student
        if(!req.getContentType().toLowerCase().startsWith("application/json")|| req.getContentType() == null){
            //send error
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        //String id  = UUID.randomUUID().toString();
        Jsonb jsonb = JsonbBuilder.create();
        StudentDto studentDTO = jsonb.fromJson(req.getReader(), StudentDto.class);
        studentDTO.setId(UtilProcess.generateId());
        System.out.println(studentDTO);
        // Persist Data
        try (var writer = resp.getWriter()){
            var ps = connection.prepareStatement(SAVE_STUDENT);
            ps.setString(1, studentDTO.getId());
            ps.setString(2, studentDTO.getName());
            ps.setString(3, studentDTO.getCity());
            ps.setString(4, studentDTO.getAge());

            if(ps.executeUpdate() != 0){
                writer.write("Student Saved");
            }else {
                writer.write("Student Not Saved");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        /*//process

        BufferedReader reader=req.getReader();
        StringBuilder stringBuilder=new StringBuilder();
        var writer=resp.getWriter();
        writer.write(stringBuilder.toString());
        reader.lines().forEach(line-> stringBuilder.append(line+"\n"));
        System.out.println(stringBuilder);

        // Jason parse

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject object = reader.readObject();
        System.out.println(object.getString("name"));



        // get values using array
        JsonReader reader1 = Json.createReader(req.getReader());
        JsonArray jArray = reader1.readArray();
        for (int i = 0; i < jArray.size(); i++) {
            JsonObject jsonObject = jArray.getJsonObject(i);
            System.out.println(jsonObject.getString("name"));
        }*/
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       //ToDo: update
        if(!req.getContentType().toLowerCase().startsWith("application/json")|| req.getContentType() == null){
            //send error
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        Jsonb jsonb = JsonbBuilder.create();
        StudentDto studentDto = jsonb.fromJson(req.getReader(), StudentDto.class);

        try(var writer = resp.getWriter()){
            var ps = connection.prepareStatement(UPDATE_STUDENT);
            ps.setString(1,studentDto.getName());
            ps.setString(2,studentDto.getCity());
            ps.setString(3,studentDto.getAge());
            ps.setString(4,studentDto.getId());

            if (ps.executeUpdate() !=0){
                writer.write("Student Updated");
            }else {
                writer.write("Student not Updated");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       //ToDo:delete
        String id = req.getParameter("id");
        try(var writer = resp.getWriter()){
            var ps = connection.prepareStatement(DELETE_STUDENT);
            ps.setString(1,id);

            if (ps.executeUpdate() !=0){
                writer.write("Student Deleted");
            }else {
                writer.write("Student not Deleted");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
