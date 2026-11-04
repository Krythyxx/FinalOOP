import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AdminWindow{
    private JTextField textField1;
    private JButton button1;
    private JPanel adminPanel;
    private JTabbedPane tabbedPane1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JTable tblAdminDashboard;

    DefaultTableModel tableAdminDashboard =new DefaultTableModel();
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
    }
    public JPanel getAdminPanel(){return adminPanel;}
}
