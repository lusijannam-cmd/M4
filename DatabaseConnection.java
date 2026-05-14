

import java.sql.*;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection("jdbc:mysql://localhost:3306/gallery_db", "root", "kyhoon0808");
    }
}

