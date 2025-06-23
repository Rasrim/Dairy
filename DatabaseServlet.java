import java.sql.*;

public class DatabaseServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // or another driver
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/yourdb", "username", "password");
            // Use the connection...
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
