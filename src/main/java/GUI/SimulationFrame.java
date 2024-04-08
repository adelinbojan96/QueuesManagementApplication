package GUI;

import BusinessLogic.SimulationManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulationFrame extends JDialog{
    private JPanel mainPanel;
    private JTextArea numberClientsTextArea;

    public JButton getButton() {
        return startButton;
    }

    private JButton startButton;
    private JLabel image;
    private JTextArea numberQueuesAvailableTextArea;

    public int getNumberQueuesAvailableTextArea() {
        String queues = numberQueuesAvailableTextArea.getText();

        try {
            return Integer.parseInt(queues);
        } catch (NumberFormatException e) {
            System.err.println("Input is not a valid integer. Returning default value.");
            return -1;
        }
    }

    public int getSimulationIntervalTextArea() {
        String simulationInterval = simulationIntervalTextArea.getText();

        try {
            return Integer.parseInt(simulationInterval);
        } catch (NumberFormatException e) {
            System.err.println("Input is not a valid integer. Returning default value.");
            return -1;
        }
    }


    public int getArrivalStartTextArea() {
        String arrivalStart = arrivalStartTextArea.getText();

        try {
            return Integer.parseInt(arrivalStart);
        } catch (NumberFormatException e) {
            System.err.println("Input is not a valid integer. Returning default value.");
            return -1;
        }
    }

    public int getArrivalEndTextArea() {
        String arrivalEnd = arrivalEndTextArea.getText();

        try {
            return Integer.parseInt(arrivalEnd);
        } catch (NumberFormatException e) {
            System.err.println("Input is not a valid integer. Returning default value.");
            return -1;
        }
    }

    public int getServiceStartTextArea() {
        String serviceStart = serviceStartTextArea.getText();

        try {
            return Integer.parseInt(serviceStart);
        } catch (NumberFormatException e) {
            System.err.println("Input is not a valid integer. Returning default value.");
            return -1;
        }
    }

    public int getServiceEndTextArea() {
        String serviceEnd = serviceEndTextArea.getText();

        try {
            return Integer.parseInt(serviceEnd);
        } catch (NumberFormatException e) {
            System.err.println("Input is not a valid integer. Returning default value.");
            return -1;
        }
    }

    private JTextArea simulationIntervalTextArea;
    private JTextArea arrivalStartTextArea;
    private JTextArea arrivalEndTextArea;
    private JTextArea serviceStartTextArea;
    private JTextArea serviceEndTextArea;

    public SimulationFrame()
    {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(650, 460);
        setTitle("Enter inputs for the operations");
        //add image for the mainPanel
        ImageIcon icon = new ImageIcon("src/pictures/shop.png");
        image.setIcon(icon);

        mainPanel.setBackground(Color.decode("#D2E3FC"));
        customizeButton(startButton);
        setContentPane(mainPanel);

        startButton.addActionListener(e -> new SimulationManager(SimulationFrame.this));

        setModal(true);
        setVisible(true);
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
