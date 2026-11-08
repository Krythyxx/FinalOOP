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
                    String password = new String(txtPassword.getPassword());


                    // checks the users table
                    String role = DbManager.authenticate(username, password);

                    if (role == null) {
                        JOptionPane.showMessageDialog(null,
                                "Access Denied: Invalid Username or Password input.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    BaseWindow window = createWindow(role, username);
                    JFrame frame = new JFrame(window.getWindowTitle());
                    JPanel panel = window.getPanel();

                    frame.setContentPane(panel);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setSize(1150, 750);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                    JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                    if (currentFrame != null) {
                        currentFrame.dispose();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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


    // picks the window class that matches the role
    private BaseWindow createWindow(String role, String username) {
        return switch (role) {
            case "admin" -> new AdminWindow(username);
            case "staff" -> new StaffWindow(username);
            default      -> new GuestWindow(username);
        };
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