import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class StaffWindow extends BaseWindow {
    private JPanel staffPanel;
    private JButton btnLogOut;
    private JLabel lblAppName;
    private JTabbedPane tbdPnStaff;
    private JPanel pnlRsrvtn;

    //For Staff Dashboard Tab
    private JTextField txtAvailableRooms;
    private JTextField txtOccupedRooms;
    private JTextField txtChkIns;
    private JTextField txtCPendignReq;
    private JTextField txtPotRevenue;
    private JScrollPane scrlPnStaffDash;
    private JTable tblStaffDashboard;

    //For Staff Reservation Tab
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
    private JTextField txtSearch;
    private JComboBox cmbFilter;
    private JButton btnAcceptRequest;
    private JButton btnDeclineRequest;
    private JButton btnCheckIn;
    private JButton btnCheckOut;
    private JButton btnCancel;
    private JButton btnDelete;
    private JTable tblStaffRsrv;
    private JTextField txtCheckIn;
    private JTextField txtCheckOut;
    private JScrollPane scrlPnStaffRsrv;

    //For Staff Rooms Tab
    private JTable tblStaffRooms;
    private JScrollPane scrlPnStaffRooms;
    private JTextField txtRoomNumber;
    private JComboBox cmbRoomType;
    private JTextField txtPrice;
    private JSpinner spnrMaxCapacity;
    private JComboBox cmbStatus;
    private JTextField txtDescription;
    private JButton btnAddRoom;
    private JButton btnUpdateSelected;
    private JButton btnDeleteSelected;
    private JLabel txtUserName;
    private JTextField txtUserRole;

    DefaultTableModel tableStaffDashboard = new DefaultTableModel();
    DefaultTableModel tableStaffReservation = new DefaultTableModel();
    DefaultTableModel tableStaffRooms = new DefaultTableModel();

    public StaffWindow(String username){
        super(username);

        //For Staff Dashboard
        initTable(tblStaffDashboard, tableStaffDashboard, DASHBOARD_COLUMNS);

        //For Staff Reservation
        initTable(tblStaffRsrv, tableStaffReservation, RESERVATION_COLUMNS);
        installFilter(tblStaffRsrv, tableStaffReservation, txtSearch, cmbFilter);

        ReservationForm form = new ReservationForm(null, tblStaffRsrv,
                txtGuestName, txtEmail, txtPhone, cmbReservationRoom, txtCheckIn, txtCheckOut,
                spnrNumberOfGuest, txtSpecialRequest, txtComments, txtBaseTotal, txtExtraCharge, txtTotal);
        bindBookingButtons(form, btnAddReservation, btnClearForm);
        bindStatusButtons(tblStaffRsrv, tableStaffReservation, form,
                btnAcceptRequest, btnDeclineRequest, btnCheckIn, btnCheckOut, btnCancel, btnDelete);

        //For Staff Rooms
        initTable(tblStaffRooms, tableStaffRooms, ROOM_COLUMNS);
        new RoomForm(tblStaffRooms, txtRoomNumber, cmbRoomType, txtPrice, spnrMaxCapacity,
                cmbStatus, txtDescription, btnAddRoom, btnUpdateSelected, btnDeleteSelected);

        //return to Main
        btnLogOut.addActionListener(e -> logOut(btnLogOut));

        loadRooms();
        loadReservations();
    }

    @Override
    protected void loadRooms() {
        fillTable(tableStaffRooms, DbManager.getAllRooms());
        loadRoomChoices(cmbReservationRoom, false);   // everything except Maintenance
        refreshDashboard(DbManager.getAllReservations());
    }

    @Override
    protected void loadReservations() {
        List<Object[]> rows = DbManager.getAllReservations();
        fillTable(tableStaffReservation, rows);
        refreshDashboard(rows);
    }

    private void refreshDashboard(List<Object[]> reservations) {
        loadDashboard(tableStaffDashboard, reservations, txtAvailableRooms, txtOccupedRooms,
                txtChkIns, txtCPendignReq, txtPotRevenue);
    }

    @Override
    public JPanel getPanel(){ return staffPanel; }

    @Override
    public String getWindowTitle(){ return "Staff"; }

    //Table Design (the styling itself is shared in BaseWindow)
    private void createUIComponents() {
        tblStaffDashboard = new JTable();
        tblStaffRsrv = new JTable();
        tblStaffRooms = new JTable();

        scrlPnStaffDash = createScrollPane(tblStaffDashboard);
        scrlPnStaffRsrv = createScrollPane(tblStaffRsrv);
        scrlPnStaffRooms = createScrollPane(tblStaffRooms);
    }
}