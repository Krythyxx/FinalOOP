import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateUserWindow {
    private JPanel createAccountPanel;
    private JTextField txtUserName;
    private JButton btnCreateAccount;
    private JPasswordField txtPassword;
    private JTextField txtName;
    private JTextField txtConfirm;
    private JButton btnBackToLogin;

    public CreateUserWindow() {

        btnCreateAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fullName = txtName.getText().trim();
                String username = txtUserName.getText().trim();
                String password = new String(txtPassword.getPassword());
                String confirm  = txtConfirm.getText();

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username and password are required.");
                    return;
                }
                if (!password.equals(confirm)) {
                    JOptionPane.showMessageDialog(null, "Passwords do not match.");
                    return;
                }

                //guest role
                String error = DbManager.addUser(fullName, username, password, "guest");
                if (error != null) {
                    JOptionPane.showMessageDialog(null, error);
                    return;
                }

                // go back to the login screen after account creation
                JOptionPane.showMessageDialog(null, "Account created! You can now log in.");
                txtName.setText("");
                txtUserName.setText("");
                txtPassword.setText("");
                txtConfirm.setText("");
                btnBackToLogin.doClick();
            }
        });

        btnBackToLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Main mainWindow = new Main();
                    JPanel loginPanel = mainWindow.getMainPanel();
                    JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(btnBackToLogin);
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

    public JPanel getCreateAccountPanel(){return createAccountPanel;}
}