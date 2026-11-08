import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

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
    private JTextField txtBaseTotal;
    private JTextField txtExtraCharge;
    private JTextField txtTotal;
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

    private TableRowSorter<DefaultTableModel> sorter;

    DefaultTableModel tableGuestReservation = new DefaultTableModel();
    DefaultTableModel tableGuestRooms = new DefaultTableModel();

    public GuestWindow(String username){
        super(username);

        //For Guest Reservation
        setColumns(tableGuestReservation, RESERVATION_COLUMNS);
        tblGuestRsrv.setModel(tableGuestReservation);
        sorter = new TableRowSorter<>(tableGuestReservation);
        tblGuestRsrv.setRowSorter(sorter);

        // fixing TextField
        styleFields(txtGuestName, txtEmail, txtPhone, txtCheckIn, txtCheckOut,
                txtSpecialRequest, txtComments, txtBaseTotal, txtExtraCharge);

        // guest name is always the logged-in username and can't be edited
        txtGuestName.setText(getUsername());
        txtGuestName.setEditable(false);

        // status filter (guests have no search box)
        cmbFilter.addActionListener(e -> applyFilter(sorter, null, cmbFilter));

        //For Guest Rooms
        setColumns(tableGuestRooms, ROOM_COLUMNS);
        tblGuestRooms.setModel(tableGuestRooms);

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
                    txtComments.getText(),
                    parseMoney(txtBaseTotal),
                    parseMoney(txtExtraCharge));

            saveReservation(rsv, "Pending");
        });

        //Clear Button
        btnClearForm.addActionListener(e -> {
            tblGuestRsrv.clearSelection();

            txtGuestName.setText(getUsername());
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

        spnrNumberOfGuest.setModel(new SpinnerNumberModel(0, 0, 30, 1));

        loadRooms();
        loadReservations();
    }

    @Override
    protected void loadRooms() {
        tableGuestRooms.setRowCount(0);
        for (Object[] row : DbManager.getAllRooms()) tableGuestRooms.addRow(row);

        cmbReservationRoom.removeAllItems();
        for (String n : DbManager.getRoomNumbers()) cmbReservationRoom.addItem(n);
    }

    // a guest only sees their own reservations
    @Override
    protected void loadReservations() {
        tableGuestReservation.setRowCount(0);
        for (Object[] row : DbManager.getReservationsByGuest(getUsername())) tableGuestReservation.addRow(row);
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