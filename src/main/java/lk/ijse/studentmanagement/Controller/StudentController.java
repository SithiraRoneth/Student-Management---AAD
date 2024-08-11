/* Created By Sithira Roneth
 * Date :6/29/24
 * Time :14:18
 * Project Name :StudentManagement
 * */
package lk.ijse.studentmanagement.Controller;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.studentmanagement.DAO.Impl.StudentDataProcess;
import lk.ijse.studentmanagement.Dto.StudentDto;
import lk.ijse.studentmanagement.Util.UtilProcess;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;

@WebServlet(urlPatterns = "/student")
public class StudentController extends HttpServlet {
    Connection connection;
    StudentDataProcess studentData = new StudentDataProcess();

    @Override
    public void init() throws ServletException {
        // ToDo : connect driver class[mysql]
        /*try {
            var driver = getServletContext().getInitParameter("driver-class");
            var dbUrl = getServletContext().getInitParameter("dbURL");
            var userName = getServletContext().getInitParameter("dbUserName");
            var password = getServletContext().getInitParameter("dbPassword");
            Class.forName(driver);
            this.connection = DriverManager.getConnection(dbUrl, userName, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }*/
        try {
            var ctx = new InitialContext();
            DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/StudentManagementAAD");
            this.connection =  pool.getConnection();
        }catch (NamingException | SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // ToDo: get details
        try (var writer = resp.getWriter()) {
            String student = studentData.getStudent(connection);
            System.out.println(student);
            writer.write(student);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Todo: Save student
        if (!req.getContentType().toLowerCase().startsWith("application/json") || req.getContentType() == null) {
            //send error
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

        // Persist Data
        try (var writer = resp.getWriter()) {
            Jsonb jsonb = JsonbBuilder.create();
            StudentDto studentDTO = jsonb.fromJson(req.getReader(), StudentDto.class);
            studentDTO.setId(UtilProcess.generateId());

            var saveStudent = studentData.saveStudent(studentDTO, connection);
            writer.write(saveStudent);

        } catch (Exception e) {
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
        if (!req.getContentType().toLowerCase().startsWith("application/json") || req.getContentType() == null) {
            //send error
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

        try (var writer = resp.getWriter()) {
            Jsonb jsonb = JsonbBuilder.create();
            StudentDto studentDto = jsonb.fromJson(req.getReader(), StudentDto.class);

            var updateStudent = studentData.updateStudent(studentDto, connection);
            writer.write(updateStudent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //ToDo:delete
        String id = req.getParameter("id");
        try (var writer = resp.getWriter()) {
            var deleteStudent = studentData.deleteStudent(id, connection);
            writer.write(deleteStudent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
