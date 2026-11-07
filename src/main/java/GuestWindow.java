package main.java;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuestWindow {
    private JPanel guestPanel;
    private JLabel lbl;
    private JButton btnLogOut;
    private JTabbedPane tabbedPane1;
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
    private JTable tblGuestRsrv;
    private JTextField textField16;
    private JComboBox comboBox4;
    private JSpinner spinner2;
    private JComboBox comboBox5;
    private JButton addRoomButton;
    private JTextField textField17;
    private JTextField textField18;
    private JButton updateSelectedButton;
    private JButton deleteSelectedButton;
    private JTable tblGuestRooms;
    private JLabel txtUserName;
    private JTextField txtUserRole;
    private JScrollPane scrlPnGuestRsrv;
    private JScrollPane scrlPnGuestRooms;


    DefaultTableModel tableGuestReservation = new DefaultTableModel();
    DefaultTableModel tableGuestRooms = new DefaultTableModel();

    public GuestWindow(){
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

        tableGuestRooms.addColumn("ID");
        tableGuestRooms.addColumn("ROOM NO.");
        tableGuestRooms.addColumn("TYPE");
        tableGuestRooms.addColumn("PRICE/NIGHT");
        tableGuestRooms.addColumn("MAX CAPACITY");
        tableGuestRooms.addColumn("STATUS");
        tableGuestRooms.addColumn("DESCRIPTION");
        tblGuestRooms.setModel(tableGuestRooms);

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
