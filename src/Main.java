import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    private JPanel mainPanel;
    private JTextField textField1;
    private JButton SIGNINButton;
    private JButton createAGuestAccountButton;
    private JPasswordField passwordField1;


    public Main() {
        SIGNINButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               try {
                   JFrame accountFrame = new JFrame("Administrator");
                   AdminWindow admin = new AdminWindow();
                   JPanel adminPanel = admin.getAdminPanel();
                   if(adminPanel == null){
                       JOptionPane.showMessageDialog(null,
                               "Error: getAdminPanel() returned null. Check deptForm.java",
                               "NULL PANEL ERROR", JOptionPane.ERROR_MESSAGE);
                   }
                   accountFrame.setContentPane(adminPanel);
                   accountFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                   accountFrame.setSize(1150,750);
                   accountFrame.setLocationRelativeTo(null);
                   accountFrame.setVisible(true);

                   JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                   if(currentFrame != null) {
                       currentFrame.dispose();
                   }
               }catch(Exception ex){ex.printStackTrace();}
            }
        });
    }
    public JPanel getMainPanel(){
        return mainPanel;
    }
    public static void main(String[] args){
        SwingUtilities.invokeLater(()->{
            JFrame frame = new JFrame("Hotel and Resort Management System");
            frame.setContentPane(new Main().mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.setSize(550,750);
        });
    }
}
