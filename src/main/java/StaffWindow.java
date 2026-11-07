import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class StaffWindow {
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
    private JTextField txtBaseTotal;
    private JTextField txtExtraCharge;
    private JTextField txtTotal;
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
    private static final String SEARCH_HINT = "Search by guest name or room";
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
    DefaultTableModel tableStaffUsers = new DefaultTableModel();
    int count = 0,xrow = 0;


    public StaffWindow(){
        tableStaffDashboard.addColumn("ID");
        tableStaffDashboard.addColumn("GUEST");
        tableStaffDashboard.addColumn("ROOM");
        tableStaffDashboard.addColumn("CHECK-IN");
        tableStaffDashboard.addColumn("CHECK-OUT");
        tableStaffDashboard.addColumn("AMOUNT");
        tableStaffDashboard.addColumn("STATUS");
        tblStaffDashboard.setModel(tableStaffDashboard);

        tableStaffReservation.addColumn("ID");
        tableStaffReservation.addColumn("GUEST");
        tableStaffReservation.addColumn("EMAIL");
        tableStaffReservation.addColumn("PHONE");
        tableStaffReservation.addColumn("ROOM");
        tableStaffReservation.addColumn("CHECK-IN");
        tableStaffReservation.addColumn("CHECK-OUT");
        tableStaffReservation.addColumn("BASE AMT");
        tableStaffReservation.addColumn("EXTRA CHARGE");
        tableStaffReservation.addColumn("TOTAL");
        tableStaffReservation.addColumn("STATUS");
        tblStaffRsrv.setModel(tableStaffReservation);
        sorter = new TableRowSorter<>(tableStaffReservation);
        tblStaffRsrv.setRowSorter(sorter);

        // fixing TextField
        for (JTextField f : new JTextField[]{txtGuestName, txtEmail, txtPhone, txtCheckIn, txtCheckOut,
                txtSpecialRequest, txtComments, txtBaseTotal, txtExtraCharge, txtSearch}) {
            f.setBorder(BorderFactory.createCompoundBorder(
                    f.getBorder(),
                    BorderFactory.createEmptyBorder(0, 8, 0, 8)));
            f.setCaretColor(Color.WHITE);
        }

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { applyFilter(); }
            public void removeUpdate(DocumentEvent e)  { applyFilter(); }
            public void changedUpdate(DocumentEvent e) { applyFilter(); }
        });
        cmbFilter.addActionListener(e -> applyFilter());

        //visual guide for search bar
        txtSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtSearch.getText().equals(SEARCH_HINT)) txtSearch.setText("");
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (txtSearch.getText().isBlank()) txtSearch.setText(SEARCH_HINT);
            }
        });

        tableStaffRooms.addColumn("ID");
        tableStaffRooms.addColumn("ROOM NO.");
        tableStaffRooms.addColumn("TYPE");
        tableStaffRooms.addColumn("PRICE/NIGHT");
        tableStaffRooms.addColumn("MAX CAPACITY");
        tableStaffRooms.addColumn("STATUS");
        tableStaffRooms.addColumn("DESCRIPTION");
        tblStaffRooms.setModel(tableStaffRooms);

        //return to Main
        btnLogOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    Main mainWindow = new Main();
                    JPanel loginPanel = mainWindow.getMainPanel();
                    JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(btnLogOut);
                    if (currentFrame != null && loginPanel != null) {
                        currentFrame.setContentPane(loginPanel);
                        currentFrame.setSize(550, 750);
                        currentFrame.setTitle("Hotel and Resort Management System");
                        currentFrame.setLocationRelativeTo(null);
                        currentFrame.revalidate();
                        currentFrame.repaint();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        //Reservation Button
        btnAddReservation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object room = cmbReservationRoom.getSelectedItem();

                Reservation rsv = new Reservation(
                        tableStaffReservation.getRowCount() + 1,
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


                String error = rsv.validate();
                if (error != null) {
                    JOptionPane.showMessageDialog(null, error);
                    return;
                }
                String dbError = DbManager.addReservation(rsv, "Pending");
                if (dbError != null) {
                    JOptionPane.showMessageDialog(null, dbError);
                    return;
                }
                loadReservations();
            }
        });

        //Clear Button
        btnClearForm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        });

        //status buttons
        btnAcceptRequest.addActionListener(e -> changeStatus("Confirmed", "Pending"));
        btnDeclineRequest.addActionListener(e -> changeStatus("Declined", "Pending"));
        btnCheckIn.addActionListener(e -> changeStatus("Checked In", "Confirmed"));
        btnCheckOut.addActionListener(e -> changeStatus("Checked Out", "Checked In"));
        btnCancel.addActionListener(e -> changeStatus("Cancelled", "Pending", "Confirmed"));
        btnDelete.addActionListener(e -> {
            int id = selectedId(tblStaffRsrv);
            if (id == -1) return;
            if (JOptionPane.showConfirmDialog(null, "Delete this reservation permanently?", "Confirm Delete",
                    JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
            String error = DbManager.deleteReservation(id);
            if (error != null) JOptionPane.showMessageDialog(null, error);
            loadReservations();
            btnClearForm.doClick();
        });


        tblStaffRsrv.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {

                if (!event.getValueIsAdjusting() && tblStaffRsrv.getSelectedRow() != -1) {

                    int selectedRow = tblStaffRsrv.getSelectedRow();

                    //Extract from rows
                    Object col1 = tblStaffRsrv.getValueAt(selectedRow, 1);
                    Object col2 = tblStaffRsrv.getValueAt(selectedRow, 2);
                    Object col3 = tblStaffRsrv.getValueAt(selectedRow, 3);
                    Object col4 = tblStaffRsrv.getValueAt(selectedRow, 4);
                    Object col5 = tblStaffRsrv.getValueAt(selectedRow, 5);
                    Object col6 = tblStaffRsrv.getValueAt(selectedRow, 6);
                    Object col7 = tblStaffRsrv.getValueAt(selectedRow, 7);
                    Object col8 = tblStaffRsrv.getValueAt(selectedRow, 8);
                    Object col9 = tblStaffRsrv.getValueAt(selectedRow, 9);

                    //Moves to Text Field
                    txtGuestName.setText(col1 != null ? col1.toString() : "");
                    txtEmail.setText(col2 != null ? col2.toString() : "");
                    txtPhone.setText(col3 != null ? col3.toString() : "");
                    cmbReservationRoom.setSelectedItem(col4 != null ? col4.toString() : "");
                    txtCheckIn.setText(col5 != null ? col5.toString() : "");
                    txtCheckOut.setText(col6 != null ? col6.toString() : "");
                    txtBaseTotal.setText(col7 != null ? col7.toString() : "");
                    txtExtraCharge.setText(col8 != null ? col8.toString() : "");
                    txtTotal.setText(col9 != null ? col9.toString() : "");
                }
            }
        });

        //Database (runs here, not in createUIComponents, because the form components don't exist yet there)
        spnrNumberOfGuest.setModel(new SpinnerNumberModel(0, 0, 30, 1));

        loadRooms();
        loadReservations();

    }

    // handles statuses to make sense, and saves the new status to the database
    private void changeStatus(String newStatus, String... allowedFrom) {
        int id = selectedId(tblStaffRsrv);
        if (id == -1) return;

        int modelRow = tblStaffRsrv.convertRowIndexToModel(tblStaffRsrv.getSelectedRow());
        String current = String.valueOf(tableStaffReservation.getValueAt(modelRow, 10));

        for (String allowed : allowedFrom) {
            if (allowed.equalsIgnoreCase(current)) {
                String error = DbManager.updateReservationStatus(id, newStatus);
                if (error != null) {
                    JOptionPane.showMessageDialog(null, error);
                    return;
                }
                loadReservations();
                return;
            }
        }
        JOptionPane.showMessageDialog(null,
                "Cannot change status to \"" + newStatus + "\" when it is \"" + current + "\".");
    }

    // Searching in Table
    private void applyFilter() {
        List<RowFilter<Object, Object>> filters = new ArrayList<>();

        //only searches the name and room type
        String text = txtSearch.getText().trim();
        if (!text.isEmpty() && !text.equals(SEARCH_HINT)) {
            filters.add(RowFilter.regexFilter("(?i)" + Pattern.quote(text), 1, 4));
        }

        //all status
        if (cmbFilter.getSelectedIndex() > 0) {
            String status = cmbFilter.getSelectedItem().toString();
            filters.add(RowFilter.regexFilter("(?i)^" + Pattern.quote(status) + "$", 10));
        }

        sorter.setRowFilter(filters.isEmpty() ? null : RowFilter.andFilter(filters));
    }

    //convert string to double
    private double parseMoney(JTextField f) {
        try {
            return Double.parseDouble(f.getText().trim());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    //Adds Db Table row
    private void loadRooms() {
        cmbReservationRoom.removeAllItems();
        for (String n : DbManager.getRoomNumbers()) cmbReservationRoom.addItem(n);
    }

    private void loadReservations() {
        tableStaffReservation.setRowCount(0);
        for (Object[] row : DbManager.getAllReservations()) tableStaffReservation.addRow(row);
    }

    //Returns the ID of the selected row, or -1 if nothing is selected
    private int selectedId(JTable table) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Please select a row first.");
            return -1;
        }
        return (int) table.getValueAt(row, 0);
    }

    //Table Design
    public JPanel getStaffPanel(){return staffPanel;}

    private void createUIComponents() {
        tblStaffDashboard = new JTable();
        JTableHeader hdrStaffDashboard = tblStaffDashboard.getTableHeader();
        hdrStaffDashboard.setOpaque(true);
        hdrStaffDashboard.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {

                Component StaffDashboard = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                StaffDashboard.setBackground(new Color(40, 50, 60));
                StaffDashboard.setForeground(Color.WHITE);
                StaffDashboard.setFont(new Font("SansSerif", Font.BOLD, 12));
                setHorizontalAlignment(JLabel.CENTER);
                return StaffDashboard;
            }
        });

        tblStaffRsrv = new JTable();
        JTableHeader hdrStaffRsrv = tblStaffRsrv.getTableHeader();
        hdrStaffRsrv.setOpaque(true);
        hdrStaffRsrv.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {

                Component StaffRsrv = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                StaffRsrv.setBackground(new Color(40, 50, 60));
                StaffRsrv.setForeground(Color.WHITE);
                StaffRsrv.setFont(new Font("SansSerif", Font.BOLD, 12));
                setHorizontalAlignment(JLabel.CENTER);
                return StaffRsrv;
            }
        });

        tblStaffRooms = new JTable();
        JTableHeader hdrAdminRooms = tblStaffRooms.getTableHeader();
        hdrAdminRooms.setOpaque(true);
        hdrAdminRooms.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {

                Component adminRooms = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                adminRooms.setBackground(new Color(40, 50, 60));
                adminRooms.setForeground(Color.WHITE);
                adminRooms.setFont(new Font("SansSerif", Font.BOLD, 12));
                setHorizontalAlignment(JLabel.CENTER);
                return adminRooms;
            }
        });

        scrlPnStaffDash = new JScrollPane(tblStaffDashboard);
        scrlPnStaffDash.getViewport().setBackground(new Color(30, 40, 50));
        scrlPnStaffDash.setBackground(new Color(30, 40, 50));

        scrlPnStaffRsrv = new JScrollPane(tblStaffRsrv);
        scrlPnStaffRsrv.getViewport().setBackground(new Color(30, 40, 50));
        scrlPnStaffRsrv.setBackground(new Color(30, 40, 50));

        scrlPnStaffRooms = new JScrollPane(tblStaffRooms);
        scrlPnStaffRooms.getViewport().setBackground(new Color(30, 40, 50));
        scrlPnStaffRooms.setBackground(new Color(30, 40, 50));


    }
}