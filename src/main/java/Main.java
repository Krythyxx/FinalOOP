import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Main {
    private JPanel mainPanel;
    private JTextField txtUsername;
    private JButton btnSignIn;
    private JButton btnCreateAcc;
    private JPasswordField txtPassword;


    public Main() {

        for (JTextField f : new JTextField[]{txtUsername, txtPassword}) {
            f.setBorder(BorderFactory.createCompoundBorder(
                    f.getBorder(),
                    BorderFactory.createEmptyBorder(0, 8, 0, 8)));
            f.setCaretColor(Color.WHITE);
        }

       btnSignIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               try {
                   String username = txtUsername.getText().trim();

                   //Admin Window
                   if(username.equals("admin")){ //database
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

                       //Staff Window
                   } else if (username.equals("staff")) { //database
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

                       //Guest Window
                   } else if (username.equals("guest")) { //database
                       JFrame guestFrame = new JFrame("Guest");
                       GuestWindow guest = new GuestWindow();
                       JPanel guestPanel = guest.getGuestPanel();
                       if(guestPanel == null){
                           JOptionPane.showMessageDialog(null,
                                   "Error: getAdminPanel() returned null. Check deptForm.java",
                                   "NULL PANEL ERROR", JOptionPane.ERROR_MESSAGE);
                       }
                       guestFrame.setContentPane(guestPanel);
                       guestFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                       guestFrame.setSize(1150,750);
                       guestFrame.setLocationRelativeTo(null);
                       guestFrame.setVisible(true);

                       JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                       if(currentFrame != null) {
                           currentFrame.dispose();
                       }
                   } else
                       JOptionPane.showMessageDialog(null, "Access Denied: Invalid Username or Password input.", "Error", JOptionPane.ERROR_MESSAGE);

               }catch(Exception ex){ex.printStackTrace();}
            }
        });
        btnCreateAcc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame CreateAccount = new JFrame("Guest");
                CreateUserWindow newAcc = new CreateUserWindow();
                JPanel CreateAccountPanel = newAcc.getCreateAccountPanel();
                if (CreateAccountPanel == null) {
                    JOptionPane.showMessageDialog(null,
                            "Error: getAdminPanel() returned null. Check deptForm.java",
                            "NULL PANEL ERROR", JOptionPane.ERROR_MESSAGE);
                }
                CreateAccount.setContentPane(CreateAccountPanel);
                CreateAccount.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                CreateAccount.setSize(550, 750);
                CreateAccount.setLocationRelativeTo(null);
                CreateAccount.setVisible(true);

                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                if (currentFrame != null) {
                    currentFrame.dispose();
                }
            }
        });
    }


    //Get Main Panel
    public JPanel getMainPanel(){
        return mainPanel;
    }

    //Main Method
    public static void main(String[] args){
        DbManager.init();
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