package main.java;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class DbManager {
    private static final String URL = "jdbc:sqlite:hotel.db";

    //Db Connection
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        try (Statement st = conn.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");
        }
        return conn;
    }

    public static void init() {
        String users = """
                CREATE TABLE IF NOT EXISTS users (
                    id         INTEGER PRIMARY KEY AUTOINCREMENT,
                    username   TEXT UNIQUE NOT NULL,
                    password   TEXT NOT NULL,
                    role       TEXT NOT NULL DEFAULT 'guest'
                               CHECK (role IN ('admin','staff','guest')),
                    full_name  TEXT,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP
                )""";

        String rooms = """
                CREATE TABLE IF NOT EXISTS rooms (
                    id              INTEGER PRIMARY KEY AUTOINCREMENT,
                    room_number     TEXT UNIQUE NOT NULL,
                    room_type       TEXT NOT NULL
                                    CHECK (room_type IN ('Standard','Deluxe','Suite','Presidential')),
                    price_per_night REAL NOT NULL,
                    capacity        INTEGER DEFAULT 2,
                    status          TEXT DEFAULT 'Available'
                                    CHECK (status IN ('Available','Occupied','Maintenance')),
                    description     TEXT
                )""";

        String reservations = """
                CREATE TABLE IF NOT EXISTS reservations (
                    id           INTEGER PRIMARY KEY AUTOINCREMENT,
                    guest_name   TEXT NOT NULL,
                    guest_email  TEXT,
                    guest_phone  TEXT,
                    room_id      INTEGER NOT NULL,
                    check_in     TEXT NOT NULL,
                    check_out    TEXT NOT NULL,
                    guests       INTEGER DEFAULT 1,
                    base_amount  REAL,
                    extra_charge REAL DEFAULT 0,
                    total_amount REAL,
                    status       TEXT DEFAULT 'Pending'
                                 CHECK (status IN ('Pending','Confirmed','Checked In',
                                                   'Checked Out','Cancelled','Declined')),
                    notes        TEXT,
                    comments     TEXT,
                    created_at   TEXT DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (room_id) REFERENCES rooms(id)
                )""";

        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            st.execute(users);
            st.execute(rooms);
            st.execute(reservations);

            st.execute("""
                    INSERT INTO users (username, password, role, full_name)
                    SELECT 'admin', 'admin123', 'admin', 'Administrator'
                    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin')""");

            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM rooms");
            if (rs.next() && rs.getInt(1) == 0) {
                st.execute("""
                        INSERT INTO rooms (room_number, room_type, price_per_night, capacity, description) VALUES
                        ('101','Standard',1200,2,'Cozy standard room with garden view'),
                        ('102','Standard',1200,2,'Standard room with city view'),
                        ('201','Deluxe',2200,3,'Spacious deluxe room with balcony'),
                        ('202','Deluxe',2200,3,'Deluxe room with pool view'),
                        ('301','Suite',4500,4,'Luxury suite with living area'),
                        ('302','Suite',4500,4,'Corner suite with panoramic view'),
                        ('401','Presidential',9500,6,'Presidential suite, full floor')""");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//Add/Delete method for "User"

    public static List<Object[]> getAllUsers() {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT id, full_name, username, role, created_at FROM users ORDER BY id";
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                rows.add(new Object[]{
                        rs.getInt("id"), rs.getString("full_name"),
                        rs.getString("username"), rs.getString("role"),
                        rs.getString("created_at")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    public static String addUser(String fullName, String username, String password, String role) {
        if (username.isBlank() || password.isBlank()) return "Username and password are required.";
        String sql = "INSERT INTO users (full_name, username, password, role) VALUES (?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fullName.isBlank() ? username : fullName);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setString(4, role);
            ps.executeUpdate();
            return null;
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")) return "Username already exists.";
            return "Database error: " + e.getMessage();
        }
    }

    //the admin account is protected from the delete feature. same as in main.py
    public static String deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ? AND username <> 'admin'";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 0 ? "The admin account cannot be deleted." : null;
        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }


//Add/Delete method for "Rooms"

    public static List<Object[]> getAllRooms() {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT id, room_number, room_type, price_per_night, capacity, status, "
                + "COALESCE(description,'') FROM rooms ORDER BY room_number";
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                rows.add(new Object[]{
                        rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getDouble(4), rs.getInt(5), rs.getString(6), rs.getString(7)});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

//for filling the room combo box in the reservation form

    public static List<String> getRoomNumbers() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT room_number FROM rooms WHERE status <> 'Maintenance' ORDER BY room_number";
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(rs.getString(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String addRoom(String roomNumber, String type, double price,
                                 int capacity, String status, String description) {
        if (roomNumber.isBlank()) return "Room number is required.";
        if (price <= 0) return "Price must be greater than 0.";
        String sql = "INSERT INTO rooms (room_number, room_type, price_per_night, capacity, status, description) "
                + "VALUES (?,?,?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roomNumber);
            ps.setString(2, type);
            ps.setDouble(3, price);
            ps.setInt(4, capacity);
            ps.setString(5, status);
            ps.setString(6, description);
            ps.executeUpdate();
            return null;
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")) return "That room number already exists.";
            return "Database error: " + e.getMessage();
        }
    }

    public static String deleteRoom(int id) {
        String sql = "DELETE FROM rooms WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return null;
        } catch (SQLException e) {
            if (e.getMessage().contains("FOREIGN KEY"))
                return "This room has reservations, so it can't be deleted. Delete those first.";
            return "Database error: " + e.getMessage();
        }
    }

//ADD/DELETE method for "RESERVATIONS"

    public static List<Object[]> getAllReservations() {
        List<Object[]> rows = new ArrayList<>();
        String sql = """
                SELECT r.id, r.guest_name, r.guest_email, r.guest_phone, rm.room_number,
                       r.check_in, r.check_out, r.base_amount, r.extra_charge,
                       r.total_amount, r.status
                FROM reservations r JOIN rooms rm ON r.room_id = rm.id
                ORDER BY r.id DESC""";
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                rows.add(new Object[]{
                        rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5),
                        LocalDate.parse(rs.getString(6)).format(Reservation.DATE_FMT),
                        LocalDate.parse(rs.getString(7)).format(Reservation.DATE_FMT),
                        rs.getDouble(8), rs.getDouble(9), rs.getDouble(10), rs.getString(11)});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    //Status: "Confirmed" for staff/admin direct booking, "Pending" for guest requests

    public static String addReservation(Reservation r, String status) {
        try {
            LocalDate in = LocalDate.parse(r.checkIn, Reservation.DATE_FMT);
            LocalDate out = LocalDate.parse(r.checkOut, Reservation.DATE_FMT);
            long nights = ChronoUnit.DAYS.between(in, out);

            try (Connection conn = getConnection()) {

                //Find the room (r.room holds the room number, e.g. "101")
                int roomId;
                double price;
                int capacity;
                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT id, price_per_night, capacity FROM rooms WHERE room_number = ?")) {
                    ps.setString(1, r.room);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) return "Room " + r.room + " does not exist.";
                    roomId = rs.getInt(1);
                    price = rs.getDouble(2);
                    capacity = rs.getInt(3);
                }

                //Double-booking check (same idea as main.py)
                try (PreparedStatement ps = conn.prepareStatement("""
                        SELECT check_in, check_out FROM reservations
                        WHERE room_id = ? AND status IN ('Confirmed','Checked In')
                          AND NOT (check_out <= ? OR check_in >= ?)""")) {
                    ps.setInt(1, roomId);
                    ps.setString(2, in.toString());
                    ps.setString(3, out.toString());
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) return "Room " + r.room + " is already booked from "
                            + rs.getString(1) + " to " + rs.getString(2) + ".";
                }

                double base = nights * price;
                double extra = Math.max(0, r.numberOfGuests - capacity) * 200 * nights;

                try (PreparedStatement ps = conn.prepareStatement("""
                        INSERT INTO reservations
                          (guest_name, guest_email, guest_phone, room_id, check_in, check_out,
                           guests, base_amount, extra_charge, total_amount, status, notes, comments)
                        VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)""")) {
                    ps.setString(1, r.guestName);
                    ps.setString(2, r.email);
                    ps.setString(3, r.phone);
                    ps.setInt(4, roomId);
                    ps.setString(5, in.toString());     // stored as yyyy-MM-dd
                    ps.setString(6, out.toString());
                    ps.setInt(7, r.numberOfGuests);
                    ps.setDouble(8, base);
                    ps.setDouble(9, extra);
                    ps.setDouble(10, base + extra);
                    ps.setString(11, status);
                    ps.setString(12, r.specialRequest);
                    ps.setString(13, r.comments);
                    ps.executeUpdate();
                }
            }
            return null;
        } catch (Exception e) {
            return "Database error: " + e.getMessage();
        }
    }

    public static String deleteReservation(int id) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM reservations WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return null;
        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }
}//Closes the DbManager class