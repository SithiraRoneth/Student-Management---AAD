/* Created By Sithira Roneth
 * Date :7/13/24
 * Time :10:14
 * Project Name :StudentManagment
 * */
package lk.ijse.studentmanagment.Util;

import java.util.UUID;

public class UtilProcess {
    public static String generateId(){
        return UUID.randomUUID().toString();
    }
}
