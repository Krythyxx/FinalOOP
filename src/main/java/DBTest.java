package main.java;

import java.sql.*;
import java.util.Arrays;

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
        // add a user: prints null if it worked
        System.out.println(DbManager.addUser("Test Guest", "testguest", "pass123", "guest"));

// add the same username again: should print "Username already exists."
        System.out.println(DbManager.addUser("Test Guest", "testguest", "pass123", "guest"));

// show every user
        for (Object[] row : DbManager.getAllUsers()) {
            System.out.println(Arrays.toString(row));
        }

// try deleting the admin: should print "The admin account cannot be deleted."
        System.out.println(DbManager.deleteUser(1));
    }
}