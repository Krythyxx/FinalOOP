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
    private JScrollPane scrlPnAdminDash;
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
    private JTable tblAdminRooms;
    private JLabel txtUserName;
    private JTextField txtUserRole;

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
        JTableHeader hdrAdminDashboard = tblStaffDashboard.getTableHeader();
        hdrAdminDashboard.setOpaque(true);
        hdrAdminDashboard.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(40, 50, 60));
                c.setForeground(Color.WHITE);
                c.setFont(new Font("SansSerif", Font.BOLD, 12));
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });
        scrlPnAdminDash = new JScrollPane(tblStaffDashboard);
        scrlPnAdminDash.getViewport().setBackground(new Color(30, 40, 50));
        scrlPnAdminDash.setBackground(new Color(30, 40, 50));
    }
}
