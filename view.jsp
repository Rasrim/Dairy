<%@ page import="java.sql.*" %>
<html>
<head><title>View Diary Entries</title></head>
<body>
    <h2>My Diary Entries</h2>
    <%
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/diarydb", "root", "your_password");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM diary_entries ORDER BY entry_date DESC");

            while (rs.next()) {
                String date = rs.getString("entry_date");
                String text = rs.getString("entry_text");
    %>
                <div style="border:1px solid black; margin:10px; padding:10px;">
                    <b><%= date %></b><br>
                    <pre><%= text %></pre>
                </div>
    <%
            }
        } catch(Exception e) {
            out.println("Error: " + e.getMessage());
        }
    %>
</body>
</html>