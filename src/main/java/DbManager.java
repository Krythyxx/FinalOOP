package main.java;

import java.sql.*;

public class DbManager {
    private static final String URL = "jdbc:sqlite:hotel.db";

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

            // default admin + starter rooms (only if empty)
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
}