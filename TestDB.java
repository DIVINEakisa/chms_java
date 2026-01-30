import java.sql.*;
public class TestDB {
    public static void main(String[] args) {
        try {
            Class.forName(\"com.mysql.cj.jdbc.Driver\");
            Connection conn = DriverManager.getConnection(\"jdbc:mysql://localhost:3306/chms_db\", \"root\", \"\");
            System.out.println(\" Database connection successful!\");
            
            PreparedStatement stmt = conn.prepareStatement(\"SELECT email, password_hash FROM users WHERE email=?\");
            stmt.setString(1, \"admin@chms.com\");
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                System.out.println(\" User found: \" + rs.getString(\"email\"));
                System.out.println(\" Hash starts with: \" + rs.getString(\"password_hash\").substring(0, 20));
            } else {
                System.out.println(\" User not found\");
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(\" Error: \" + e.getMessage());
            e.printStackTrace();
        }
    }
}
