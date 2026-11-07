import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class GuestWindow {
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
    private JButton btnAcceptRequest;
    private JButton btnDeclineRequest;
    private JButton btnCheckIn;
    private JButton btnCheckOut;
    private JButton btnCancel;
    private JButton btnDelete;
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

    public GuestWindow(){
        //For Guest Reservation
        tableGuestReservation.addColumn("ID");
        tableGuestReservation.addColumn("GUEST");
        tableGuestReservation.addColumn("EMAIL");
        tableGuestReservation.addColumn("PHONE");
        tableGuestReservation.addColumn("ROOM");
        tableGuestReservation.addColumn("CHECK-IN");
        tableGuestReservation.addColumn("CHECK-OUT");
        tableGuestReservation.addColumn("BASE AMT");
        tableGuestReservation.addColumn("EXTRA CHARGE");
        tableGuestReservation.addColumn("TOTAL");
        tableGuestReservation.addColumn("STATUS");
        tblGuestRsrv.setModel(tableGuestReservation);
        sorter = new TableRowSorter<>(tableGuestReservation);
        tblGuestRsrv.setRowSorter(sorter);

        // fixing TextField
        for (JTextField f : new JTextField[]{txtGuestName, txtEmail, txtPhone, txtCheckIn, txtCheckOut,
                txtSpecialRequest, txtComments, txtBaseTotal, txtExtraCharge}) {
            f.setBorder(BorderFactory.createCompoundBorder(
                    f.getBorder(),
                    BorderFactory.createEmptyBorder(0, 8, 0, 8)));
            f.setCaretColor(Color.WHITE);
        }

        // search bar and admin status buttons are not used by guests
        btnAcceptRequest.setVisible(false);
        btnDeclineRequest.setVisible(false);
        btnCheckIn.setVisible(false);
        btnCheckOut.setVisible(false);
        btnCancel.setVisible(false);
        btnDelete.setVisible(false);

        // status filter
        cmbFilter.addActionListener(e -> applyFilter());

        //For Guest Rooms
        tableGuestRooms.addColumn("ID");
        tableGuestRooms.addColumn("ROOM NO.");
        tableGuestRooms.addColumn("TYPE");
        tableGuestRooms.addColumn("PRICE/NIGHT");
        tableGuestRooms.addColumn("MAX CAPACITY");
        tableGuestRooms.addColumn("STATUS");
        tableGuestRooms.addColumn("DESCRIPTION");
        tblGuestRooms.setModel(tableGuestRooms);

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

        //Reservation Button (always saved as Pending)
        btnAddReservation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object room = cmbReservationRoom.getSelectedItem();

                Reservation rsv = new Reservation(
                        tableGuestReservation.getRowCount() + 1,
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
                tblGuestRsrv.clearSelection();

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

        tblGuestRsrv.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {

                if (!event.getValueIsAdjusting() && tblGuestRsrv.getSelectedRow() != -1) {

                    int selectedRow = tblGuestRsrv.getSelectedRow();

                    //Extract from rows
                    Object col1 = tblGuestRsrv.getValueAt(selectedRow, 1);
                    Object col2 = tblGuestRsrv.getValueAt(selectedRow, 2);
                    Object col3 = tblGuestRsrv.getValueAt(selectedRow, 3);
                    Object col4 = tblGuestRsrv.getValueAt(selectedRow, 4);
                    Object col5 = tblGuestRsrv.getValueAt(selectedRow, 5);
                    Object col6 = tblGuestRsrv.getValueAt(selectedRow, 6);
                    Object col7 = tblGuestRsrv.getValueAt(selectedRow, 7);
                    Object col8 = tblGuestRsrv.getValueAt(selectedRow, 8);
                    Object col9 = tblGuestRsrv.getValueAt(selectedRow, 9);

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

        spnrNumberOfGuest.setModel(new SpinnerNumberModel(0, 0, 30, 1));

        loadRooms();
        loadReservations();
    }

    // Filter table by status
    private void applyFilter() {
        if (cmbFilter.getSelectedIndex() > 0) {
            String status = cmbFilter.getSelectedItem().toString();
            sorter.setRowFilter(RowFilter.regexFilter("(?i)^" + Pattern.quote(status) + "$", 10));
        } else {
            sorter.setRowFilter(null);
        }
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
        tableGuestRooms.setRowCount(0);
        for (Object[] row : DbManager.getAllRooms()) tableGuestRooms.addRow(row);

        cmbReservationRoom.removeAllItems();
        for (String n : DbManager.getRoomNumbers()) cmbReservationRoom.addItem(n);
    }

    private void loadReservations() {
        tableGuestReservation.setRowCount(0);
        for (Object[] row : DbManager.getAllReservations()) tableGuestReservation.addRow(row);
    }

    public JPanel getGuestPanel(){return guestPanel;}

    private void createUIComponents() {
        tblGuestRsrv = new JTable();
        JTableHeader hdrAdminRsrv = tblGuestRsrv.getTableHeader();
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

        tblGuestRooms = new JTable();
        JTableHeader hdrAdminRooms = tblGuestRooms.getTableHeader();
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

        scrlPnGuestRsrv = new JScrollPane(tblGuestRsrv);
        scrlPnGuestRsrv.getViewport().setBackground(new Color(30, 40, 50));
        scrlPnGuestRsrv.setBackground(new Color(30, 40, 50));

        scrlPnGuestRooms = new JScrollPane(tblGuestRooms);
        scrlPnGuestRooms.getViewport().setBackground(new Color(30, 40, 50));
        scrlPnGuestRooms.setBackground(new Color(30, 40, 50));
    }
}