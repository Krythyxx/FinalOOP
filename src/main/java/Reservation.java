import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Reservation {
    public static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    private final int id;
    private final String guestName, email, phone, room;
    private final String checkIn, checkOut;
    private final int numberOfGuests;
    private final String specialRequest, comments;

    public Reservation(int id, String guestName, String email, String phone, String room,
                       String checkIn, String checkOut, int numberOfGuests,
                       String specialRequest, String comments) {
        this.id = id; this.guestName = guestName; this.email = email; this.phone = phone;
        this.room = room; this.checkIn = checkIn; this.checkOut = checkOut;
        this.numberOfGuests = numberOfGuests; this.specialRequest = specialRequest;
        this.comments = comments;
    }

    //getters
    public int getId() { return id; }
    public String getGuestName() { return guestName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getRoom() { return room; }
    public String getCheckIn() { return checkIn; }
    public String getCheckOut() { return checkOut; }
    public int getNumberOfGuests() { return numberOfGuests; }
    public String getSpecialRequest() { return specialRequest; }
    public String getComments() { return comments; }

    //returns an error message, or null when everything is valid
    public String validate() {
        if (guestName.isBlank()) return "Guest name is required.";
        if (!email.matches("^[\\w.+-]+@[\\w-]+\\.[\\w.]+$")) return "Enter a valid email.";
        if (!phone.matches("\\d{7,13}")) return "Phone must be 7-13 digits.";
        if (room.isBlank()) return "Select a room.";
        if (numberOfGuests < 1) return "Enter at least 1 guest.";
        if (checkIn.isBlank() || checkOut.isBlank()) return "Enter check-in and check-out dates.";

        LocalDate in, out;
        try {
            in = LocalDate.parse(checkIn, DATE_FMT);
            out = LocalDate.parse(checkOut, DATE_FMT);
        } catch (Exception ex) {
            return "Dates must be in MM-dd-yyyy format (example: 11-15-2026).";
        }
        if (in.isBefore(LocalDate.now())) return "Check-in cannot be before today.";
        if (!out.isAfter(in)) return "Check-out must be after check-in.";
        return null;
    }
}
