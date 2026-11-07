import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Reservation {

    //date format
    static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    int id;
    String guestName, email, phone, room;
    String checkIn, checkOut;
    int numberOfGuests;
    String specialRequest, comments;
    double baseAmount, extraCharge;
    String status = "Pending";

    public Reservation() {}

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
        if (guestName == null || guestName.isBlank()) return "Guest name is required.";
        if (email == null || !email.matches("^[\\w.+-]+@[\\w-]+\\.[\\w.]+$")) return "Enter a valid email.";
        if (phone == null || !phone.matches("\\d{7,13}")) return "Phone must be 7-13 digits.";
        if (checkIn == null || checkIn.isBlank() || checkOut == null || checkOut.isBlank())
            return "Select check-in and check-out dates.";

        //date format validation
        try {
            LocalDate in = LocalDate.parse(checkIn, DATE_FMT);
            LocalDate out = LocalDate.parse(checkOut, DATE_FMT);
            if (in.isBefore(LocalDate.now())) return "Check-in cannot be in the past.";
            if (!out.isAfter(in)) return "Check-out must be after check-in.";
        } catch (Exception ex) {
            return "Dates must be in MM-dd-yyyy format.";
        }
        return null;
    }

    Object[] toRow() {
        return new Object[]{ id, guestName, email, phone, room, checkIn, checkOut,
                baseAmount, extraCharge, getTotal(), status };
    }
}