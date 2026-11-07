import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;

public class AdminWindow{
    // General Elements
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
    private JComboBox cmbCheckIn;
    private JComboBox cmbCheckOut;
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
    }
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
