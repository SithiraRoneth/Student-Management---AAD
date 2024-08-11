/* Created By Sithira Roneth
 * Date :8/10/24
 * Time :15:15
 * Project Name :StudentManagment
 * */
package lk.ijse.studentmanagement.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class CORSFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        var origin = req.getHeader("Origin");
        var configOrigin = getServletContext().getInitParameter("origin");

        if (origin.contains(configOrigin)) {
            res.setHeader("Access-Control-Allow-Origin", origin);
            res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT,PATCH, DELETE, OPTIONS");
            res.setHeader("Access-Control-Allow-Headers","Content-Type");
            res.setHeader("Access-Control-Expose-Headers", "Content-Type");
        }
        chain.doFilter(req, res);
    }
}
