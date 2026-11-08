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


    //Rooms Buttons
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

    //One item of the room dropdown shows a label, remembers the room number
    public static class RoomOption {
        public final String roomNumber;
        private final String label;

        public RoomOption(String roomNumber, String label) {
            this.roomNumber = roomNumber;
            this.label = label;
        }

        @Override
        public String toString() { return label; }   // this is what the combo box displays
    }

    //If availableOnly is true
    //If availableOnly is false
    public static List<RoomOption> getRoomOptions(boolean availableOnly) {
        List<RoomOption> list = new ArrayList<>();
        String where = availableOnly ? "status = 'Available'" : "status <> 'Maintenance'";
        String sql = "SELECT room_number, room_type, price_per_night, capacity, status "
                + "FROM rooms WHERE " + where + " ORDER BY room_number";
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String number = rs.getString("room_number");
                String label = String.format("Room %s — %s (₱%,.0f/night, max:%d)",
                        number, rs.getString("room_type"),
                        rs.getDouble("price_per_night"), rs.getInt("capacity"));
                if (!availableOnly && "Occupied".equals(rs.getString("status"))) {
                    label += " [Currently Occupied]";
                }
                list.add(new RoomOption(number, label));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    //Pricing
    private static final double EXTRA_GUEST_FEE = 200;

    public static double[] computeQuote(double pricePerNight, int capacity, long nights, int guests) {
        double base = nights * pricePerNight;
        double extra = Math.max(0, guests - capacity) * EXTRA_GUEST_FEE * nights;
        return new double[]{base, extra, base + extra};
    }

    //gets the room up and prices the stay
    public static double[] getQuote(String roomNumber, LocalDate in, LocalDate out, int guests) {
        long nights = ChronoUnit.DAYS.between(in, out);
        if (nights < 1) return null;
        String sql = "SELECT price_per_night, capacity FROM rooms WHERE room_number = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roomNumber);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;
            return computeQuote(rs.getDouble(1), rs.getInt(2), nights, guests);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Max guests of a room
    public static int getCapacity(String roomNumber) {
        String sql = "SELECT capacity FROM rooms WHERE room_number = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roomNumber);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String addRoom(String roomNumber, String type, double price,
                                 int capacity, String status, String description) {
        String invalid = validateRoom(roomNumber, price);
        if (invalid != null) return invalid;
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

    private static String validateRoom(String roomNumber, double price) {
        if (roomNumber.isBlank()) return "Room number is required.";
        if (price <= 0) return "Price must be greater than 0.";
        return null;
    }

    public static String updateRoom(int id, String roomNumber, String type, double price,
                                    int capacity, String status, String description) {
        String invalid = validateRoom(roomNumber, price);
        if (invalid != null) return invalid;
        String sql = "UPDATE rooms SET room_number = ?, room_type = ?, price_per_night = ?, "
                + "capacity = ?, status = ?, description = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roomNumber);
            ps.setString(2, type);
            ps.setDouble(3, price);
            ps.setInt(4, capacity);
            ps.setString(5, status);
            ps.setString(6, description);
            ps.setInt(7, id);
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

    //add and delete for reservations
    private static final String RESERVATION_SELECT = """
            SELECT r.id, r.guest_name, r.guest_email, r.guest_phone, rm.room_number,
                   r.check_in, r.check_out, r.base_amount, r.extra_charge,
                   r.total_amount, r.status
            FROM reservations r JOIN rooms rm ON r.room_id = rm.id""";

    public static List<Object[]> getAllReservations() {
        return queryReservations("", null);
    }

    //limits guest name as their username
    public static List<Object[]> getReservationsByGuest(String guestName) {
        return queryReservations("WHERE r.guest_name = ? ", guestName);
    }

    //runs the select and turns each row into the tables columns
    private static List<Object[]> queryReservations(String where, String guestName) {
        List<Object[]> rows = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(RESERVATION_SELECT + " " + where + "ORDER BY r.id DESC")) {
            if (guestName != null) ps.setString(1, guestName);
            ResultSet rs = ps.executeQuery();
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

    public static String addReservation(Reservation r, String status) {
        try {
            LocalDate in = LocalDate.parse(r.getCheckIn(), Reservation.DATE_FMT);
            LocalDate out = LocalDate.parse(r.getCheckOut(), Reservation.DATE_FMT);
            long nights = ChronoUnit.DAYS.between(in, out);

            try (Connection conn = getConnection()) {

                //Find the room
                int roomId;
                double price;
                int capacity;
                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT id, price_per_night, capacity FROM rooms WHERE room_number = ?")) {
                    ps.setString(1, r.getRoom());
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) return "Room " + r.getRoom() + " does not exist.";
                    roomId = rs.getInt(1);
                    price = rs.getDouble(2);
                    capacity = rs.getInt(3);
                }

                //Double-booking check
                try (PreparedStatement ps = conn.prepareStatement("""
                        SELECT check_in, check_out FROM reservations
                        WHERE room_id = ? AND status IN ('Confirmed','Checked In')
                          AND NOT (check_out <= ? OR check_in >= ?)""")) {
                    ps.setInt(1, roomId);
                    ps.setString(2, in.toString());
                    ps.setString(3, out.toString());
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) return "Room " + r.getRoom() + " is already booked from "
                            + rs.getString(1) + " to " + rs.getString(2) + ".";
                }

                double[] quote = computeQuote(price, capacity, nights, r.getNumberOfGuests());

                try (PreparedStatement ps = conn.prepareStatement("""
                        INSERT INTO reservations
                          (guest_name, guest_email, guest_phone, room_id, check_in, check_out,
                           guests, base_amount, extra_charge, total_amount, status, notes, comments)
                        VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)""")) {
                    ps.setString(1, r.getGuestName());
                    ps.setString(2, r.getEmail());
                    ps.setString(3, r.getPhone());
                    ps.setInt(4, roomId);
                    ps.setString(5, in.toString());
                    ps.setString(6, out.toString());
                    ps.setInt(7, r.getNumberOfGuests());
                    ps.setDouble(8, quote[0]);
                    ps.setDouble(9, quote[1]);
                    ps.setDouble(10, quote[2]);
                    ps.setString(11, status);
                    ps.setString(12, r.getSpecialRequest());
                    ps.setString(13, r.getComments());
                    ps.executeUpdate();
                }
            }
            return null;
        } catch (Exception e) {
            return "Database error: " + e.getMessage();
        }
    }

    public static String updateReservationStatus(int id, String status) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE reservations SET status = ? WHERE id = ?")) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
            return null;
        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }


    //Login check
    public static String authenticate(String username, String password) {
        String sql = "SELECT role FROM users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString(1) : null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
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

    //Numbers for the Dashboard tab
    public static class DashboardStats {
        public final int availableRooms, occupiedRooms, checkInsToday, pendingRequests;
        public final double potentialRevenue;

        DashboardStats(int availableRooms, int occupiedRooms, int checkInsToday,
                       int pendingRequests, double potentialRevenue) {
            this.availableRooms = availableRooms;
            this.occupiedRooms = occupiedRooms;
            this.checkInsToday = checkInsToday;
            this.pendingRequests = pendingRequests;
            this.potentialRevenue = potentialRevenue;
        }
    }

    public static DashboardStats getDashboardStats() {
        String sql = """
                SELECT (SELECT COUNT(*) FROM rooms WHERE status <> 'Maintenance'),
                       (SELECT COUNT(DISTINCT room_id) FROM reservations WHERE status = 'Checked In'),
                       (SELECT COUNT(*) FROM reservations
                         WHERE status IN ('Confirmed','Checked In') AND check_in = ?),
                       (SELECT COUNT(*) FROM reservations WHERE status = 'Pending'),
                       (SELECT COALESCE(SUM(total_amount), 0) FROM reservations
                         WHERE status IN ('Pending','Confirmed'))""";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, LocalDate.now().toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int occupied = rs.getInt(2);
                return new DashboardStats(Math.max(0, rs.getInt(1) - occupied), occupied,
                        rs.getInt(3), rs.getInt(4), rs.getDouble(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new DashboardStats(0, 0, 0, 0, 0);
    }
}//Closes the DbManager class