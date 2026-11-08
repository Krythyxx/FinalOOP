import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GuestWindow extends BaseWindow {
    private JPanel guestPanel;
    private JLabel lbl;
    private JButton btnLogOut;
    private JTabbedPane tabbedPane1;
    private JPanel pnlRsrvtn;
    private JTextField txtGuestName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JSpinner spnrNumberOfGuest;
    private JTextField txtSpecialRequest;
    private JTextField txtComments;
    private JTextArea txtBaseTotal;
    private JTextArea txtExtraCharge;
    private JTextArea txtTotal;
    private JButton btnAddReservation;
    private JButton btnClearForm;
    private JComboBox cmbReservationRoom;
    private JComboBox cmbFilter;
    private JTable tblGuestRsrv;
    private JTable tblGuestRooms;
    private JLabel txtUserName;
    private JTextField txtUserRole;
    private JScrollPane scrlPnGuestRsrv;
    private JScrollPane scrlPnGuestRooms;
    private JTextField txtCheckIn;
    private JTextField txtCheckOut;

    DefaultTableModel tableGuestReservation = new DefaultTableModel();
    DefaultTableModel tableGuestRooms = new DefaultTableModel();

    public GuestWindow(String username){
        super(username);

        //For Guest Reservation
        initTable(tblGuestRsrv, tableGuestReservation, RESERVATION_COLUMNS);
        installFilter(tblGuestRsrv, tableGuestReservation, null, cmbFilter);

        //For Guest Rooms
        initTable(tblGuestRooms, tableGuestRooms, ROOM_COLUMNS);

        //the guest name is always their username and cant be edited
        ReservationForm form = new ReservationForm(getUsername(), tblGuestRsrv,
                txtGuestName, txtEmail, txtPhone, cmbReservationRoom, txtCheckIn, txtCheckOut,
                spnrNumberOfGuest, txtSpecialRequest, txtComments, txtBaseTotal, txtExtraCharge, txtTotal);
        bindBookingButtons(form, btnAddReservation, btnClearForm);

        //return to Main
        btnLogOut.addActionListener(e -> logOut(btnLogOut));

        loadRooms();
        loadReservations();
    }

    @Override
    protected void loadRooms() {
        fillTable(tableGuestRooms, DbManager.getAllRooms());
        loadRoomChoices(cmbReservationRoom, true);   // guests only see Available rooms
    }

    // a guest only sees their own reservations
    @Override
    protected void loadReservations() {
        fillTable(tableGuestReservation, DbManager.getReservationsByGuest(getUsername()));
    }

    @Override
    public JPanel getPanel(){ return guestPanel; }

    @Override
    public String getWindowTitle(){ return "Guest"; }

    //Table Design (the styling itself is shared in BaseWindow)
    private void createUIComponents() {
        tblGuestRsrv = new JTable();
        tblGuestRooms = new JTable();

        scrlPnGuestRsrv = createScrollPane(tblGuestRsrv);
        scrlPnGuestRooms = createScrollPane(tblGuestRooms);
    }
}