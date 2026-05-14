import java.sql.*;
import java.util.ArrayList;

public class ExhibitDAO {
    public ArrayList<String> getAll() {
        ArrayList<String> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT name FROM exhibits")) {
            while (rs.next())
                list.add(rs.getString("name"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void add(String name) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement("INSERT INTO exhibits (name) VALUES (?)")) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String name) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement("DELETE FROM exhibits WHERE name = ?")) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}