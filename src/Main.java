import javax.swing.*;

public class Main {
    private JPanel mainPanel;
    private JLabel txtTest;
    private JButton testButton;

    public static void main(String[] args) {
        int score = 75;

        if (score >= 90) {
            System.out.println("Grade: A");
        } else if (score >= 75) {
            System.out.println("Grade: B");
        } else {
            System.out.println("Grade: C or below");
        }
    }
}