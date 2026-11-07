package main.java;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AdminWindow{
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
    private JTextField txtBaseTotal;
    private JTextField txtExtraCharge;
    private JTextField txtTotal;
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
    private static final String SEARCH_HINT = "Search by guest name or room";

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
    int count = 0,xrow = 0;


    public AdminWindow(){

        //For Admin Dashboard
        tableAdminDashboard.addColumn("ID");
        tableAdminDashboard.addColumn("GUEST");
        tableAdminDashboard.addColumn("ROOM");
        tableAdminDashboard.addColumn("CHECK-IN");
        tableAdminDashboard.addColumn("CHECK-OUT");
        tableAdminDashboard.addColumn("AMOUNT");
        tableAdminDashboard.addColumn("STATUS");
        tblAdminDashboard.setModel(tableAdminDashboard);


        //For Admin Reservation
        tableAdminReservation.addColumn("ID");
        tableAdminReservation.addColumn("GUEST");
        tableAdminReservation.addColumn("EMAIL");
        tableAdminReservation.addColumn("PHONE");
        tableAdminReservation.addColumn("ROOM");
        tableAdminReservation.addColumn("CHECK-IN");
        tableAdminReservation.addColumn("CHECK-OUT");
        tableAdminReservation.addColumn("BASE AMT");
        tableAdminReservation.addColumn("EXTRA CHARGE");
        tableAdminReservation.addColumn("TOTAL");
        tableAdminReservation.addColumn("STATUS");
        tblAdminRsrv.setModel(tableAdminReservation);
        sorter = new TableRowSorter<>(tableAdminReservation);
        tblAdminRsrv.setRowSorter(sorter);

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

        tableAdminRooms.addColumn("ID");
        tableAdminRooms.addColumn("ROOM NO.");
        tableAdminRooms.addColumn("TYPE");
        tableAdminRooms.addColumn("PRICE/NIGHT");
        tableAdminRooms.addColumn("MAX CAPACITY");
        tableAdminRooms.addColumn("STATUS");
        tableAdminRooms.addColumn("DESCRIPTION");
        tblAdminRooms.setModel(tableAdminRooms);

        tableAdminUsers.addColumn("ID");
        tableAdminUsers.addColumn("FULL NAME");
        tableAdminUsers.addColumn("USERNAME");
        tableAdminUsers.addColumn("ROLE");
        tableAdminUsers.addColumn("DATE CREATED");
        tblAdminUsers.setModel(tableAdminUsers);

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
                        tableAdminReservation.getRowCount() + 1,
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
                tableAdminReservation.addRow(rsv.toRow());
            }
        });

        //Clear Button
        btnClearForm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        });

        //status buttons
        btnAcceptRequest.addActionListener(e -> changeStatus("Confirmed", "Pending"));
        btnDeclineRequest.addActionListener(e -> changeStatus("Declined", "Pending"));
        btnCheckIn.addActionListener(e -> changeStatus("Checked in", "Confirmed"));
        btnCheckOut.addActionListener(e -> changeStatus("Checked Out", "Checked in"));
        btnCancel.addActionListener(e -> changeStatus("Cancelled", "Pending", "Confirmed"));
        btnDelete.addActionListener(e -> {
            int viewRow = tblAdminRsrv.getSelectedRow();
            if (viewRow == -1) {
                JOptionPane.showMessageDialog(null, "Select a reservation first.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Delete this reservation?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int modelRow = tblAdminRsrv.convertRowIndexToModel(viewRow);
                tableAdminReservation.removeRow(modelRow);
                btnClearForm.doClick();
            }
        });


        tblAdminRsrv.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {

                if (!event.getValueIsAdjusting() && tblAdminRsrv.getSelectedRow() != -1) {

                    int selectedRow = tblAdminRsrv.getSelectedRow();

                    //Extract from rows
                    Object col1 = tblAdminRsrv.getValueAt(selectedRow, 1);
                    Object col2 = tblAdminRsrv.getValueAt(selectedRow, 2);
                    Object col3 = tblAdminRsrv.getValueAt(selectedRow, 3);
                    Object col4 = tblAdminRsrv.getValueAt(selectedRow, 4);
                    Object col5 = tblAdminRsrv.getValueAt(selectedRow, 5);
                    Object col6 = tblAdminRsrv.getValueAt(selectedRow, 6);
                    Object col7 = tblAdminRsrv.getValueAt(selectedRow, 7);
                    Object col8 = tblAdminRsrv.getValueAt(selectedRow, 8);
                    Object col9 = tblAdminRsrv.getValueAt(selectedRow, 9);

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

    }

    // handles statuses to make sense
    private void changeStatus(String newStatus, String... allowedFrom) {
        int viewRow = tblAdminRsrv.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(null, "Select a reservation first.");
            return;
        }

        int modelRow = tblAdminRsrv.convertRowIndexToModel(viewRow);
        String current = String.valueOf(tableAdminReservation.getValueAt(modelRow, 10));

        for (String allowed : allowedFrom) {
            if (allowed.equalsIgnoreCase(current)) {
                tableAdminReservation.setValueAt(newStatus, modelRow, 10);
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

    //Table Design
    public JPanel getAdminPanel(){return adminPanel;}

    private void createUIComponents() {

        tblAdminDashboard = new JTable();
        JTableHeader hdrAdminDashboard = tblAdminDashboard.getTableHeader();
        hdrAdminDashboard.setOpaque(true);
        hdrAdminDashboard.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component adminDash = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                adminDash.setBackground(new Color(40, 50, 60));
                adminDash.setForeground(Color.WHITE);
                adminDash.setFont(new Font("SansSerif", Font.BOLD, 12));
                setHorizontalAlignment(JLabel.CENTER);
                return adminDash;
            }
        });

        tblAdminRsrv = new JTable();
        JTableHeader hdrAdminRsrv = tblAdminRsrv.getTableHeader();
        hdrAdminRsrv.setOpaque(true);
        hdrAdminRsrv.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {

                Component adminRsrv = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                adminRsrv.setBackground(new Color(40, 50, 60));
                adminRsrv.setForeground(Color.WHITE);
                adminRsrv.setFont(new Font("SansSerif", Font.BOLD, 12));
                setHorizontalAlignment(JLabel.CENTER);
                return adminRsrv;
            }
        });


        tblAdminRooms = new JTable();
        JTableHeader hdrAdminRooms = tblAdminRooms.getTableHeader();
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


        tblAdminUsers = new JTable();
        JTableHeader hdrAdminUsers = tblAdminUsers.getTableHeader();
        hdrAdminUsers.setOpaque(true);
        hdrAdminUsers.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {

                Component adminUsers = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                adminUsers.setBackground(new Color(40, 50, 60));
                adminUsers.setForeground(Color.WHITE);
                adminUsers.setFont(new Font("SansSerif", Font.BOLD, 12));
                setHorizontalAlignment(JLabel.CENTER);
                return adminUsers;
            }
        });

        scrlPnAdminDash = new JScrollPane(tblAdminDashboard);
        scrlPnAdminDash.getViewport().setBackground(new Color(30, 40, 50));
        scrlPnAdminDash.setBackground(new Color(30, 40, 50));

        scrlPnAdminRsrv = new JScrollPane(tblAdminRsrv);
        scrlPnAdminRsrv.getViewport().setBackground(new Color(30, 40, 50));
        scrlPnAdminRsrv.setBackground(new Color(30, 40, 50));

        scrlPnAdminRooms = new JScrollPane(tblAdminRooms);
        scrlPnAdminRooms.getViewport().setBackground(new Color(30, 40, 50));
        scrlPnAdminRooms.setBackground(new Color(30, 40, 50));

        scrlPnAdminUsers = new JScrollPane(tblAdminUsers);
        scrlPnAdminUsers.getViewport().setBackground(new Color(30, 40, 50));
        scrlPnAdminUsers.setBackground(new Color(30, 40, 50));
    }
}