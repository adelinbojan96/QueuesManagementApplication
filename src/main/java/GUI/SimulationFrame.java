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
    private JTextArea numberQueuesAvailableTextArea;

    public int getNumberClients() {
        String people = numberClientsTextArea.getText();
        try {
            return Integer.parseInt(people);
        } catch (NumberFormatException e) {
            System.err.println("Input is not a valid integer. Returning default value.");
            return -1;
        }
    }
    public int getNumberQueuesAvailable() {
        String queues = numberQueuesAvailableTextArea.getText();

        try {
            return Integer.parseInt(queues);
        } catch (NumberFormatException e) {
            System.err.println("Input is not a valid integer. Returning default value.");
            return -1;
        }
    }

    public int getSimulationInterval() {
        String simulationInterval = simulationIntervalTextArea.getText();

        try {
            return Integer.parseInt(simulationInterval);
        } catch (NumberFormatException e) {
            System.err.println("Input is not a valid integer. Returning default value.");
            return -1;
        }
    }


    public int getArrivalStart() {
        String arrivalStart = arrivalStartTextArea.getText();

        try {
            return Integer.parseInt(arrivalStart);
        } catch (NumberFormatException e) {
            System.err.println("Input is not a valid integer. Returning default value.");
            return -1;
        }
    }

    public int getArrivalEnd() {
        String arrivalEnd = arrivalEndTextArea.getText();

        try {
            return Integer.parseInt(arrivalEnd);
        } catch (NumberFormatException e) {
            System.err.println("Input is not a valid integer. Returning default value.");
            return -1;
        }
    }

    public int getServiceStart() {
        String serviceStart = serviceStartTextArea.getText();

        try {
            return Integer.parseInt(serviceStart);
        } catch (NumberFormatException e) {
            System.err.println("Input is not a valid integer. Returning default value.");
            return -1;
        }
    }

    public int getServiceEnd() {
        String serviceEnd = serviceEndTextArea.getText();

        try {
            return Integer.parseInt(serviceEnd);
        } catch (NumberFormatException e) {
            System.err.println("Input is not a valid integer. Returning default value.");
            return -1;
        }
    }
    public String getStrategyChooser() {
        return (String) strategyChooser.getSelectedItem();
    }
    private JTextArea simulationIntervalTextArea;
    private JTextArea arrivalStartTextArea;
    private JTextArea arrivalEndTextArea;
    private JTextArea serviceStartTextArea;
    private JTextArea serviceEndTextArea;
    private JComboBox strategyChooser;

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
        customizeChooser(strategyChooser);
        setContentPane(mainPanel);

        startButton.addActionListener(e -> new QueueViewer(this));

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
    private static void customizeChooser(JComboBox operationChooser)
    {
        operationChooser.addItem("Shortest Time");
        operationChooser.addItem("Shortest Queue");
        operationChooser.setBackground(Color.WHITE);
        operationChooser.setForeground(Color.BLACK);
        operationChooser.setFont(new Font("Consolas", Font.PLAIN, 14));
        operationChooser.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1),
                BorderFactory.createEmptyBorder(3, 3, 3, 3)
        ));
    }
}
