/* Created By Sithira Roneth
 * Date :7/6/24
 * Time :10:29
 * Project Name :StudentManagment
 * */
package lk.ijse.studentmanagment.Dto;

import lombok.*;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentDto implements Serializable {
    private String id;
    private String name;
    private String city;
    private String age;
}
