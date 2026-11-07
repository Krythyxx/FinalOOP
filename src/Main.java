import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    private JPanel mainPanel;
    private JTextField txtUsername;
    private JButton btnSignIn;
    private JButton btnCreateAcc;
    private JPasswordField txtPassword;


    public Main() {
       btnSignIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               try {
                   String username = txtUsername.getText().trim();
                   if(username.equals("admin")){
                       JFrame adminFrame = new JFrame("Administrator");
                       AdminWindow admin = new AdminWindow();
                       JPanel adminPanel = admin.getAdminPanel();
                       if(adminPanel == null){
                           JOptionPane.showMessageDialog(null,
                                   "Error: getAdminPanel() returned null. Check deptForm.java",
                                   "NULL PANEL ERROR", JOptionPane.ERROR_MESSAGE);
                       }
                       adminFrame.setContentPane(adminPanel);
                       adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                       adminFrame.setSize(1150,750);
                       adminFrame.setLocationRelativeTo(null);
                       adminFrame.setVisible(true);

                       JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                       if(currentFrame != null) {
                           currentFrame.dispose();
                       }
                   } else if (username.equals("staff")) {
                       JFrame staffFrame = new JFrame("Staff");
                       StaffWindow staff = new StaffWindow();
                       JPanel staffPanel = staff.getStaffPanel();
                       if(staffPanel == null){
                           JOptionPane.showMessageDialog(null,
                                   "Error: getAdminPanel() returned null. Check deptForm.java",
                                   "NULL PANEL ERROR", JOptionPane.ERROR_MESSAGE);
                       }
                       staffFrame.setContentPane(staffPanel);
                       staffFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                       staffFrame.setSize(1150,750);
                       staffFrame.setLocationRelativeTo(null);
                       staffFrame.setVisible(true);

                       JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                       if(currentFrame != null) {
                           currentFrame.dispose();
                       }
                   } else if (username.equals("guest")) {
                       JFrame guestFrame = new JFrame("Staff");
                       GuestWindow guest = new GuestWindow();
                       JPanel guestPanel = guest.getGuestPanel();
                       if(guestPanel == null){
                           JOptionPane.showMessageDialog(null,
                                   "Error: getAdminPanel() returned null. Check deptForm.java",
                                   "NULL PANEL ERROR", JOptionPane.ERROR_MESSAGE);
                       }
                       guestFrame.setContentPane(guestPanel);
                       guestFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                       guestFrame.setLocationRelativeTo(null);
                       guestFrame.setVisible(true);

                       JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                       if(currentFrame != null) {
                           currentFrame.dispose();
                       }
                   } else {
                       JOptionPane.showMessageDialog(null, "Access Denied: Invalid Username or Password input.", "Error", JOptionPane.ERROR_MESSAGE);
                   }

               }catch(Exception ex){ex.printStackTrace();}
            }
        });
    }


    //Get Main Panel
    public JPanel getMainPanel(){
        return mainPanel;
    }


    //Main Method
    public static void main(String[] args){
        SwingUtilities.invokeLater(()->{
            JFrame frame = new JFrame("Hotel and Resort Management System");
            frame.setContentPane(new Main().mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //frame.pack();
            frame.setSize(550,750);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        });
    }
}
