import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class AdminWindow extends BaseWindow {
    private JButton btnLogOut;
    private JPanel adminPanel;
    private JTabbedPane tabbedPane1;
    private JTextField txtUserRole;

    //For Admin Dashboard Tab
    private JTextField txtAvailableRooms;
    private JTextField txtPotentialRevenue;
    private JTextField txtCheckIns;
    private JTextField txtCPendingRequest;
    private JTextField txtOccupiedRooms;
    private JTable tblAdminDashboard;

    //For Admin Reservation Tab
    private JScrollPane scrlPnAdminDash;
    private JTextField txtGuestName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JComboBox cmbReservationRoom;
    private JSpinner spnrNumberOfGuest;
    private JTextField txtSpecialRequest;
    private JTextField txtComments;
    private JTextArea txtBaseTotal;
    private JTextArea txtExtraCharge;
    private JTextArea txtTotal;
    private JButton btnAddReservation;
    private JButton btnClearForm;
    private JTextField txtSearch;
    private JComboBox cmbFilter;
    private JButton btnAcceptRequest;
    private JButton btnDeclineRequest;
    private JButton btnCheckIn;
    private JButton btnCheckOut;
    private JButton btnCancel;
    private JButton btnDelete;
    private JTable tblAdminRsrv;
    private JTextField txtCheckIn;
    private JTextField txtCheckOut;

    //For Admin Rooms Tab
    private JTable tblAdminRooms;
    private JTextField txtRoomNumber;
    private JComboBox cmbRoomType;
    private JTextField txtPrice;
    private JSpinner spnrMaxCapacity;
    private JComboBox cmbStatus;
    private JTextField txtDescription;
    private JButton btnAddRoom;
    private JButton btnUpdateSelected;
    private JButton btnDeleteSelected;

    private JTextField txtFullName;
    private JTextField txtUsername;
    private JTextField txtPassword;
    private JRadioButton rbGuest;
    private JRadioButton rbStaff;
    private JRadioButton rbAdmin;
    private JButton btnAddUser;
    private JButton btnDeleteUser;
    private JTable tblAdminUsers;
    private JLabel txtUserName;
    private JLabel lbl;
    private JPanel pnlRsrvtn;
    private JScrollPane scrlPnAdminRsrv;
    private JScrollPane scrlPnAdminRooms;
    private JScrollPane scrlPnAdminUsers;
    private JPanel tbdPanAdmin;
    private JLabel lblMaxCapacity;

    DefaultTableModel tableAdminDashboard = new DefaultTableModel();
    DefaultTableModel tableAdminReservation = new DefaultTableModel();
    DefaultTableModel tableAdminRooms = new DefaultTableModel();
    DefaultTableModel tableAdminUsers = new DefaultTableModel();

    public AdminWindow(String username){
        super(username);

        //For Admin Dashboard
        initTable(tblAdminDashboard, tableAdminDashboard, DASHBOARD_COLUMNS);

        //For Admin Reservation
        initTable(tblAdminRsrv, tableAdminReservation, RESERVATION_COLUMNS);
        installFilter(tblAdminRsrv, tableAdminReservation, txtSearch, cmbFilter);

        ReservationForm form = new ReservationForm(null, tblAdminRsrv,
                txtGuestName, txtEmail, txtPhone, cmbReservationRoom, txtCheckIn, txtCheckOut,
                spnrNumberOfGuest, txtSpecialRequest, txtComments, txtBaseTotal, txtExtraCharge, txtTotal, lblMaxCapacity);
        bindBookingButtons(form, btnAddReservation, btnClearForm);
        bindStatusButtons(tblAdminRsrv, tableAdminReservation, form,
                btnAcceptRequest, btnDeclineRequest, btnCheckIn, btnCheckOut, btnCancel, btnDelete);

        //Rooms and Users tables
        initTable(tblAdminRooms, tableAdminRooms, ROOM_COLUMNS);
        initTable(tblAdminUsers, tableAdminUsers, "ID", "FULL NAME", "USERNAME", "ROLE", "DATE CREATED");

        //return to Main
        btnLogOut.addActionListener(e -> logOut(btnLogOut));


        loadUsers();
        loadRooms();
        loadReservations();

        //add user
        btnAddUser.addActionListener(e -> {
            String role = rbAdmin.isSelected() ? "admin" : rbStaff.isSelected() ? "staff" : "guest";
            String error = DbManager.addUser(txtFullName.getText().trim(), txtUsername.getText().trim(),
                    txtPassword.getText().trim(), role);
            if (error != null) { JOptionPane.showMessageDialog(null, error); return; }
            txtFullName.setText(""); txtUsername.setText(""); txtPassword.setText("");
            loadUsers();
        });

        //delete user
        btnDeleteUser.addActionListener(e -> {
            int id = selectedId(tblAdminUsers);
            if (id == -1) return;
            if (JOptionPane.showConfirmDialog(null, "Delete this user?", "Confirm",
                    JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
            String error = DbManager.deleteUser(id);
            if (error != null) JOptionPane.showMessageDialog(null, error);
            loadUsers();
        });

        //Rooms tab
        new RoomForm(tblAdminRooms, txtRoomNumber, cmbRoomType, txtPrice, spnrMaxCapacity,
                cmbStatus, txtDescription, btnAddRoom, btnUpdateSelected, btnDeleteSelected);

    }

    //Adds Db Table row
    private void loadUsers() {
        fillTable(tableAdminUsers, DbManager.getAllUsers());
    }

    @Override
    protected void loadRooms() {
        fillTable(tableAdminRooms, DbManager.getAllRooms());
        loadRoomChoices(cmbReservationRoom, false);   // everything except Maintenance
        refreshDashboard(DbManager.getAllReservations());
    }

    @Override
    protected void loadReservations() {
        List<Object[]> rows = DbManager.getAllReservations();
        fillTable(tableAdminReservation, rows);
        refreshDashboard(rows);
    }

    private void refreshDashboard(List<Object[]> reservations) {
        loadDashboard(tableAdminDashboard, reservations, txtAvailableRooms, txtOccupiedRooms,
                txtCheckIns, txtCPendingRequest, txtPotentialRevenue);
    }

    @Override
    public JPanel getPanel(){ return adminPanel; }

    @Override
    public String getWindowTitle(){ return "Administrator"; }

    //Table Design (the styling itself is shared in BaseWindow)
    private void createUIComponents() {
        tblAdminDashboard = new JTable();
        tblAdminRsrv = new JTable();
        tblAdminRooms = new JTable();
        tblAdminUsers = new JTable();

        scrlPnAdminDash = createScrollPane(tblAdminDashboard);
        scrlPnAdminRsrv = createScrollPane(tblAdminRsrv);
        scrlPnAdminRooms = createScrollPane(tblAdminRooms);
        scrlPnAdminUsers = createScrollPane(tblAdminUsers);
    }
}