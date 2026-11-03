import javax.swing.*;

public class Main {
    private JPanel mainPanel;
    private JTextField textField1;
    private JTextField textField2;
    private JButton SIGNINButton;
    private JButton createAGuestAccountButton;

    public static void main(String[] args){
        JFrame frame = new JFrame("Hotel and Resort Management System");
        frame.setContentPane(new Main().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setSize(550,750);
    }
}
