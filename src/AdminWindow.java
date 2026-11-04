import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
    private JTextField txtPotRevenue;
    private JTextField txtChkIns;
    private JTextField txtCPendignReq;
    private JTextField txtOccupedRooms;
    private JTable tblAdminDashboard;

    //For Admin Reservation Tab
    private JScrollPane scrlPnAdminDash;
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
    private JTextField searchByGuestNameTextField;
    private JComboBox comboBox3;
    private JButton btnAcceptReq;
    private JButton btnDeclienReq;
    private JButton btnChkIn;
    private JButton btnChkOut;
    private JButton btnCancel;
    private JButton btnDelete;
    private JTable tblAdminRsrv;

    //For Admin Rooms Tab
    private JTable tblAdminRooms;
    private JTextField textField16;
    private JComboBox comboBox4;
    private JTextField textField17;
    private JSpinner spinner2;
    private JComboBox comboBox5;
    private JTextField textField18;
    private JButton addRoomButton;
    private JButton updateSelectedButton;
    private JButton deleteSelectedButton;
    private JTextField textField19;
    private JTextField textField20;
    private JTextField textField21;
    private JRadioButton staffRadioButton;
    private JRadioButton adminRadioButton;
    private JRadioButton guestRadioButton;
    private JButton addUserButton;
    private JButton deleteUserButton;
    private JTable tblAdminUsers;
    private JComboBox cmbRsrvRoom;
    private JLabel txtUserName;
    private JLabel lbl;
    private JPanel pnlRsrvtn;

    DefaultTableModel tableAdminDashboard =new DefaultTableModel();
    DefaultTableModel tableAdminReservation = new DefaultTableModel();
    int count = 0,xrow = 0;
    public AdminWindow(){
        tableAdminDashboard.addColumn("ID");
        tableAdminDashboard.addColumn("GUEST");
        tableAdminDashboard.addColumn("ROOM");
        tableAdminDashboard.addColumn("CHECK-IN");
        tableAdminDashboard.addColumn("CHECK-OUT");
        tableAdminDashboard.addColumn("AMOUNT");
        tableAdminDashboard.addColumn("STATUS");
        tblAdminDashboard.setModel(tableAdminDashboard);

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

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(40, 50, 60));
                c.setForeground(Color.WHITE);
                c.setFont(new Font("SansSerif", Font.BOLD, 12));
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });
        scrlPnAdminDash = new JScrollPane(tblAdminDashboard);
        scrlPnAdminDash.getViewport().setBackground(new Color(30, 40, 50));
        scrlPnAdminDash.setBackground(new Color(30, 40, 50));
    }
}
