import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

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
    private TableRowSorter<DefaultTableModel> sorter;

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

    DefaultTableModel tableAdminDashboard = new DefaultTableModel();
    DefaultTableModel tableAdminReservation = new DefaultTableModel();
    DefaultTableModel tableAdminRooms = new DefaultTableModel();
    DefaultTableModel tableAdminUsers = new DefaultTableModel();

    public AdminWindow(String username){
        super(username);

        //For Admin Dashboard
        setColumns(tableAdminDashboard, DASHBOARD_COLUMNS);
        tblAdminDashboard.setModel(tableAdminDashboard);

        //For Admin Reservation
        setColumns(tableAdminReservation, RESERVATION_COLUMNS);
        tblAdminRsrv.setModel(tableAdminReservation);
        sorter = new TableRowSorter<>(tableAdminReservation);
        tblAdminRsrv.setRowSorter(sorter);

        // fixing TextField
        styleFields(txtGuestName, txtEmail, txtPhone, txtCheckIn, txtCheckOut,
                txtSpecialRequest, txtComments/*, txtBaseTotal, txtExtraCharge*/, txtSearch);

        //search and filter
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { applyFilter(sorter, txtSearch, cmbFilter); }
            public void removeUpdate(DocumentEvent e)  { applyFilter(sorter, txtSearch, cmbFilter); }
            public void changedUpdate(DocumentEvent e) { applyFilter(sorter, txtSearch, cmbFilter); }
        });
        cmbFilter.addActionListener(e -> applyFilter(sorter, txtSearch, cmbFilter));
        setSearchHint(txtSearch);

        //Rooms and Users tables
        setColumns(tableAdminRooms, ROOM_COLUMNS);
        tblAdminRooms.setModel(tableAdminRooms);

        setColumns(tableAdminUsers, "ID", "FULL NAME", "USERNAME", "ROLE", "DATE CREATED");
        tblAdminUsers.setModel(tableAdminUsers);

        //return to Main
        btnLogOut.addActionListener(e -> logOut(btnLogOut));

        //Reservation Button
        btnAddReservation.addActionListener(e -> {
            Object room = cmbReservationRoom.getSelectedItem();

            Reservation rsv = new Reservation(
                    0,
                    txtGuestName.getText().trim(),
                    txtEmail.getText().trim(),
                    txtPhone.getText().trim(),
                    room == null ? "" : room.toString(),
                    txtCheckIn.getText().trim(),
                    txtCheckOut.getText().trim(),
                    (Integer) spnrNumberOfGuest.getValue(),
                    txtSpecialRequest.getText(),
                    txtComments.getText()/*,
                    parseMoney(txtBaseTotal),
                    parseMoney(txtExtraCharge)*/);

            saveReservation(rsv, "Pending");
        });

        //Clear Button
        btnClearForm.addActionListener(e -> {
            tblAdminRsrv.clearSelection();

            txtGuestName.setText("");
            txtEmail.setText("");
            txtPhone.setText("");
            txtCheckIn.setText("");
            txtCheckOut.setText("");
            txtSpecialRequest.setText("");
            txtComments.setText("");
            txtBaseTotal.setText("");
            txtExtraCharge.setText("");
            txtTotal.setText("");

            spnrNumberOfGuest.setValue(0);

            if (cmbReservationRoom.getItemCount() > 0) {
                cmbReservationRoom.setSelectedIndex(0);
            }
        });

        //status buttons
        btnAcceptRequest.addActionListener(e -> changeStatus(tblAdminRsrv, tableAdminReservation, "Confirmed", "Pending"));
        btnDeclineRequest.addActionListener(e -> changeStatus(tblAdminRsrv, tableAdminReservation, "Declined", "Pending"));
        btnCheckIn.addActionListener(e -> changeStatus(tblAdminRsrv, tableAdminReservation, "Checked In", "Confirmed"));
        btnCheckOut.addActionListener(e -> changeStatus(tblAdminRsrv, tableAdminReservation, "Checked Out", "Checked In"));
        btnCancel.addActionListener(e -> changeStatus(tblAdminRsrv, tableAdminReservation, "Cancelled", "Pending", "Confirmed"));
        btnDelete.addActionListener(e -> {
            if (deleteSelectedReservation(tblAdminRsrv)) btnClearForm.doClick();
        });

        //clicking a row moves its values to the text fields
        tblAdminRsrv.getSelectionModel().addListSelectionListener(event -> {
            int row = tblAdminRsrv.getSelectedRow();
            if (event.getValueIsAdjusting() || row == -1) return;

            txtGuestName.setText(cellText(tblAdminRsrv, row, 1));
            txtEmail.setText(cellText(tblAdminRsrv, row, 2));
            txtPhone.setText(cellText(tblAdminRsrv, row, 3));
            cmbReservationRoom.setSelectedItem(cellText(tblAdminRsrv, row, 4));
            txtCheckIn.setText(cellText(tblAdminRsrv, row, 5));
            txtCheckOut.setText(cellText(tblAdminRsrv, row, 6));
            txtBaseTotal.setText(cellText(tblAdminRsrv, row, 7));
            txtExtraCharge.setText(cellText(tblAdminRsrv, row, 8));
            txtTotal.setText(cellText(tblAdminRsrv, row, 9));
        });

        spnrNumberOfGuest.setModel(new SpinnerNumberModel(0, 0, 30, 1));
        spnrMaxCapacity.setModel(new SpinnerNumberModel(2, 1, 20, 1));

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

        //Rooms
        btnAddRoom.addActionListener(e -> {
            try {
                double price = Double.parseDouble(txtPrice.getText().trim());
                String error = DbManager.addRoom(txtRoomNumber.getText().trim(),
                        cmbRoomType.getSelectedItem().toString(), price,
                        (Integer) spnrMaxCapacity.getValue(),
                        cmbStatus.getSelectedItem().toString(), txtDescription.getText().trim());
                if (error != null) { JOptionPane.showMessageDialog(null, error); return; }
                txtRoomNumber.setText(""); txtPrice.setText(""); txtDescription.setText("");
                loadRooms();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Price must be a number.");
            }
        });

        //delete room
        btnDeleteSelected.addActionListener(e -> {
            int id = selectedId(tblAdminRooms);
            if (id == -1) return;
            if (JOptionPane.showConfirmDialog(null, "Delete this room?", "Confirm",
                    JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
            String error = DbManager.deleteRoom(id);
            if (error != null) JOptionPane.showMessageDialog(null, error);
            loadRooms();
        });

    }

    //Adds Db Table row
    private void loadUsers() {
        tableAdminUsers.setRowCount(0);
        for (Object[] row : DbManager.getAllUsers()) tableAdminUsers.addRow(row);
    }

    @Override
    protected void loadRooms() {
        tableAdminRooms.setRowCount(0);
        for (Object[] row : DbManager.getAllRooms()) tableAdminRooms.addRow(row);

        cmbReservationRoom.removeAllItems();
        for (String n : DbManager.getRoomNumbers()) cmbReservationRoom.addItem(n);
    }

    @Override
    protected void loadReservations() {
        tableAdminReservation.setRowCount(0);
        for (Object[] row : DbManager.getAllReservations()) tableAdminReservation.addRow(row);
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