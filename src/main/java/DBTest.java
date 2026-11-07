import java.sql.*;

public class DBTest {
    public static void main(String[] args) {
        DbManager.init();

        try (Connection conn = DbManager.getConnection();
             Statement st = conn.createStatement()) {

            ResultSet rs = st.executeQuery("SELECT room_number, room_type, price_per_night FROM rooms");
            while (rs.next()) {
                System.out.println(rs.getString("room_number") + " | "
                        + rs.getString("room_type") + " | "
                        + rs.getDouble("price_per_night"));
            }

            rs = st.executeQuery("SELECT username, role FROM users");
            while (rs.next()) {
                System.out.println("User: " + rs.getString("username") + " (" + rs.getString("role") + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}