import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


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
    private JTextField txtCreateRsrv;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JComboBox cmbChkIn;
    private JComboBox cmbChkOut;
    private JSpinner spnrGuestNum;
    private JTextField txtSpReq;
    private JTextField txtComments;
    private JTextField txtBaseTotal;
    private JTextField txtXtraCharge;
    private JTextField textField14;
    private JButton addReservationButton;
    private JButton clearFormButton;
    private JComboBox cmbRsrvRoom;
    private JTextField searchByGuestNameTextField;
    private JComboBox comboBox3;
    private JButton btnAcceptReq;
    private JButton btnDeclienReq;
    private JButton btnChkIn;
    private JButton btnChkOut;
    private JButton btnCancel;
    private JButton btnDelete;
    private JTable tblStaffRsrv;
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

        tableStaffRooms.addColumn("ID");
        tableStaffRooms.addColumn("ROOM NO.");
        tableStaffRooms.addColumn("TYPE");
        tableStaffRooms.addColumn("PRICE/NIGHT");
        tableStaffRooms.addColumn("MAX CAPACITY");
        tableStaffRooms.addColumn("STATUS");
        tableStaffRooms.addColumn("DESCRIPTION");
        tblStaffRooms.setModel(tableStaffRooms);

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
