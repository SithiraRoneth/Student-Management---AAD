/* Created By Sithira Roneth
 * Date :7/13/24
 * Time :13:03
 * Project Name :StudentManagment
 * */
package lk.ijse.studentmanagment.DAO.Impl;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import lk.ijse.studentmanagment.DAO.StudentData;
import lk.ijse.studentmanagment.Dto.StudentDto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDataProcess implements StudentData {
    static String SAVE_STUDENT = "INSERT INTO student (id,name,city,age) VALUES (?,?,?,?)";
    static String UPDATE_STUDENT = "UPDATE student SET name = ? , city = ? , age = ? WHERE id = ?";
    static String DELETE_STUDENT = "DELETE FROM student WHERE id = ? ";
    static String GET_STUDENT = "SELECT * FROM student";

    @Override
    public String getStudent(Connection connection) {
        var studentDto = new StudentDto();

        try{
            var ps = connection.prepareStatement(GET_STUDENT);

            ResultSet resultSet = ps.executeQuery();
            List<StudentDto> studentDtoList = new ArrayList<>();

            while (resultSet.next()) {
                studentDto.setId(resultSet.getString("id"));
                studentDto.setName(resultSet.getString("name"));
                studentDto.setCity(resultSet.getString("city"));
                studentDto.setAge(resultSet.getString("age"));
                studentDtoList.add(studentDto);

            }
            Jsonb jsonb = JsonbBuilder.create();
            return jsonb.toJson(studentDtoList);

        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String saveStudent(StudentDto studentDto, Connection connection) {
        try {
             var ps = connection.prepareStatement(SAVE_STUDENT);

             ps.setString(1,studentDto.getId());
             ps.setString(2,studentDto.getName());
             ps.setString(3,studentDto.getCity());
             ps.setString(4,studentDto.getAge());

             if (ps.executeUpdate() !=0){
                 return "Student Saved Successfully";
             }else {
                 return "Unsuccessful";
             }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String deleteStudent(String id, Connection connection) {
       try{
           var ps = connection.prepareStatement(DELETE_STUDENT);
           ps.setString(1,id);

           if (ps.executeUpdate() !=0){
               return "Deleted Successfully";
           }else {
               return "Deleted Unsuccessful";
           }
       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
    }

    @Override
    public String updateStudent(StudentDto studentDto, Connection connection) {
        try {
            var ps = connection.prepareStatement(UPDATE_STUDENT);
            ps.setString(1,studentDto.getName());
            ps.setString(2,studentDto.getCity());
            ps.setString(3,studentDto.getAge());
            ps.setString(4,studentDto.getId());

            if (ps.executeUpdate() !=0){
                return "Update Successfully";
            }else {
                return "Update Unsuccessful";
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
