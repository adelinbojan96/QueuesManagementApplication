package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class SimulationFrame extends JDialog{
    private JPanel mainPanel;
    private JTextArea numberClientsTextArea;
    private JButton startButton;
    private JLabel image;

    public SimulationFrame()
    {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(650, 460);
        setTitle("Enter inputs for the operations");
        ImageIcon icon = new ImageIcon("src/pictures/shop.png");
        image.setIcon(icon);
        //customizeButton(startButton);
        setContentPane(mainPanel);
        setModal(true);
        setVisible(true);
    }
    public static void main(String[] args) {
        //access the gui_frame
        new SimulationFrame();
    }
    private static void customizeButton(JButton button)
    {
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.BLACK, 2, true),
                new EmptyBorder(5, 20, 5, 20)
        ));
    }
}
