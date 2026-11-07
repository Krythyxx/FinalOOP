import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Reservation {
    static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    int id;
    String guestName, email, phone, room;
    String checkIn, checkOut;
    int numberOfGuests;
    String specialRequest, comments;
    double baseAmount, extraCharge;
    String status = "Pending";

    public Reservation(int id, String guestName, String email, String phone, String room,
                       String checkIn, String checkOut, int numberOfGuests,
                       String specialRequest, String comments,
                       double baseAmount, double extraCharge) {
        this.id = id; this.guestName = guestName; this.email = email; this.phone = phone;
        this.room = room; this.checkIn = checkIn; this.checkOut = checkOut;
        this.numberOfGuests = numberOfGuests; this.specialRequest = specialRequest;
        this.comments = comments; this.baseAmount = baseAmount; this.extraCharge = extraCharge;
    }

    double getTotal() { return baseAmount + extraCharge; }

    String validate() {
        if (guestName.isBlank()) return "Guest name is required.";
        if (!email.matches("^[\\w.+-]+@[\\w-]+\\.[\\w.]+$")) return "Enter a valid email.";
        if (!phone.matches("\\d{7,13}")) return "Phone must be 7-13 digits.";
        if (room.isBlank()) return "Select a room.";
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

    Object[] toRow() {
        return new Object[]{ id, guestName, email, phone, room, checkIn, checkOut,
                baseAmount, extraCharge, getTotal(), status };
    }
}