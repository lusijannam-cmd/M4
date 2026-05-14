import java.sql.*;
import java.util.ArrayList;

public class UserDAO {
    public void save(User u) {
        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPassword());
            ps.setString(4, u.getRole());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<User> getAll() {

        ArrayList<User> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM users")) {
            while (rs.next()) {
                list.add(new User.Builder().setName(rs.getString("name")).setEmail(rs.getString("email"))
                        .setPassword(rs.getString("password")).setRole(rs.getString("role")).build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}