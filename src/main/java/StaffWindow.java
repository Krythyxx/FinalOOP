import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class StaffWindow extends BaseWindow {
    private JPanel staffPanel;
    private JLabel lblAppName;
    private JButton btnLogOut;
    private JTabbedPane tbdPnStaff;
    private JTextField txtAvailableRooms;
    private JTextField txtOccupedRooms;
    private JTextField txtChkIns;
    private JTextField txtCPendignReq;
    private JTextField txtPotRevenue;
    private JScrollPane scrlPnStaffDash;
    private JTable tblStaffDashboard;
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
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField textField16;
    private JComboBox comboBox4;
    private JSpinner spinner2;
    private JComboBox comboBox5;
    private JButton addRoomButton;
    private JTextField textField17;
    private JTextField textField18;
    private JButton updateSelectedButton;
    private JButton deleteSelectedButton;
    private JTable tblStaffRooms;
    private JLabel txtUserName;
    private JTextField txtUserRole;
    private JScrollPane scrlPnStaffRsrv;
    private JScrollPane scrlPnStaffRooms;

    DefaultTableModel tableStaffDashboard =new DefaultTableModel();
    DefaultTableModel tableStaffReservation = new DefaultTableModel();
    DefaultTableModel tableStaffRooms = new DefaultTableModel();

    public StaffWindow(String username){
        super(username);

        setColumns(tableStaffDashboard, DASHBOARD_COLUMNS);
        tblStaffDashboard.setModel(tableStaffDashboard);

        setColumns(tableStaffReservation, RESERVATION_COLUMNS);
        tblStaffRsrv.setModel(tableStaffReservation);
        sorter = new TableRowSorter<>(tableStaffReservation);
        tblStaffRsrv.setRowSorter(sorter);

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
        setSearchHint(txtSearch);   //visual guide for search bar

        setColumns(tableStaffRooms, ROOM_COLUMNS);
        tblStaffRooms.setModel(tableStaffRooms);

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
            tblStaffRsrv.clearSelection();

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
        btnAcceptRequest.addActionListener(e -> changeStatus(tblStaffRsrv, tableStaffReservation, "Confirmed", "Pending"));
        btnDeclineRequest.addActionListener(e -> changeStatus(tblStaffRsrv, tableStaffReservation, "Declined", "Pending"));
        btnCheckIn.addActionListener(e -> changeStatus(tblStaffRsrv, tableStaffReservation, "Checked In", "Confirmed"));
        btnCheckOut.addActionListener(e -> changeStatus(tblStaffRsrv, tableStaffReservation, "Checked Out", "Checked In"));
        btnCancel.addActionListener(e -> changeStatus(tblStaffRsrv, tableStaffReservation, "Cancelled", "Pending", "Confirmed"));
        btnDelete.addActionListener(e -> {
            if (deleteSelectedReservation(tblStaffRsrv)) btnClearForm.doClick();
        });

        //Clicking a row moves its values to the text fields
        tblStaffRsrv.getSelectionModel().addListSelectionListener(event -> {
            int row = tblStaffRsrv.getSelectedRow();
            if (event.getValueIsAdjusting() || row == -1) return;

            txtGuestName.setText(cellText(tblStaffRsrv, row, 1));
            txtEmail.setText(cellText(tblStaffRsrv, row, 2));
            txtPhone.setText(cellText(tblStaffRsrv, row, 3));
            cmbReservationRoom.setSelectedItem(cellText(tblStaffRsrv, row, 4));
            txtCheckIn.setText(cellText(tblStaffRsrv, row, 5));
            txtCheckOut.setText(cellText(tblStaffRsrv, row, 6));
            txtBaseTotal.setText(cellText(tblStaffRsrv, row, 7));
            txtExtraCharge.setText(cellText(tblStaffRsrv, row, 8));
            txtTotal.setText(cellText(tblStaffRsrv, row, 9));
        });

        //Database
        spnrNumberOfGuest.setModel(new SpinnerNumberModel(0, 0, 30, 1));

        loadRooms();
        loadReservations();
    }

    @Override
    protected void loadRooms() {
        tableStaffRooms.setRowCount(0);
        for (Object[] row : DbManager.getAllRooms()) tableStaffRooms.addRow(row);

        cmbReservationRoom.removeAllItems();
        for (String n : DbManager.getRoomNumbers()) cmbReservationRoom.addItem(n);
    }

    @Override
    protected void loadReservations() {
        tableStaffReservation.setRowCount(0);
        for (Object[] row : DbManager.getAllReservations()) tableStaffReservation.addRow(row);
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