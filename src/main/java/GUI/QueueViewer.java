package GUI;

import BusinessLogic.SimulationManager;
import Model.Task;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class QueueViewer extends JDialog{
    private final SimulationFrame frame;
    private JPanel mainFrame;
    private final JProgressBar[] progressBar;
    private JLabel timeLabel;
    private JLabel averageWaitingTime;
    private JLabel averageServiceTime;
    private JLabel peakHour;

    public QueueViewer(SimulationFrame simulationFrame)
    {
        this.frame = simulationFrame;
        this.progressBar = new JProgressBar[simulationFrame.getNumberQueuesAvailable() + 1];
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setTitle("See the progress for each client in the queues");
        setSize(650, 460);

        mainFrame = new JPanel();
        mainFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainFrame.setBackground(Color.decode("#fdfa72"));
        setContentPane(mainFrame);

        JScrollPane scrollPane = new JScrollPane(mainFrame);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setContentPane(scrollPane);

        addTextFields(gbc);

        createVisualQueues(gbc);

        new SimulationManager(simulationFrame, this);
        setModal(true);
        setVisible(true);
    }
    private void addTextFields(GridBagConstraints gbc)
    {
        JLabel infoLabel = new JLabel("Queues Progress");
        customizeLabelFont(infoLabel);
        mainFrame.add(infoLabel, gbc);

        gbc.gridy++;

        averageWaitingTime = new JLabel("Average Waiting Time: 0");
        customizeLabelFontSmaller(averageWaitingTime);
        mainFrame.add(averageWaitingTime, gbc);

        gbc.gridy++;

        averageServiceTime = new JLabel("Average Service Time: 0");
        customizeLabelFontSmaller(averageServiceTime);
        mainFrame.add(averageServiceTime, gbc);

        gbc.gridy++;

        peakHour = new JLabel("Peak hour: 0");
        customizeLabelFontSmaller(peakHour);
        mainFrame.add(peakHour, gbc);

        gbc.gridy++;

        timeLabel = new JLabel("Time: 0");
        customizeLabelFontSmaller(timeLabel);
        mainFrame.add(timeLabel, gbc);
    }
    private void createVisualQueues(GridBagConstraints gbc)
    {
        int numberQueues = this.frame.getNumberQueuesAvailable();
        for (int i = 0; i < numberQueues; i++) {
            progressBar[i] = new JProgressBar();
            progressBar[i].setString("Queue " + (i + 1));
            progressBar[i].setStringPainted(true);
            progressBar[i].setMinimum(0);
            progressBar[i].setMaximum(100);
            progressBar[i].setValue(0);
            customizeQueue(progressBar[i]);
            mainFrame.add(progressBar[i]);

            gbc.gridx = 0;
            gbc.gridy += 3;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.insets = new Insets(10, 10, 10, 10);
            mainFrame.add(progressBar[i], gbc);
        }
    }
    private void customizeQueue(JProgressBar progressBar)
    {
        Dimension prefSize = progressBar.getPreferredSize();
        prefSize.width = 300; // Adjust the width as desired
        progressBar.setPreferredSize(prefSize);

        progressBar.setBackground(Color.decode("#ADD8E6"));

        progressBar.setStringPainted(true);
        progressBar.setForeground(Color.decode("#21B6A8"));
    }
    private void customizeLabelFont(JLabel label)
    {
        Font font = new Font("Consolas", Font.BOLD, 16);
        label.setFont(font);
    }
    private void customizeLabelFontSmaller(JLabel label)
    {
        Font font = new Font("Consolas", Font.PLAIN, 14);
        label.setFont(font);
    }
    private int simple3RuleForCalculatingPercentage(int simulationTime, int currentTime)
    {
        /*
             x         currentTime
            ---   =   --------------
            100       simulationTime
         */
        return 100 * currentTime / simulationTime;
    }
    public void updateTimingText(int currentTime)
    {
        this.timeLabel.setText("Time: " + currentTime);
    }

    public void updateProgress(int queueIndex, int simulationTime, int personalizedTime, Task currentTask)
    {
        progressBar[queueIndex - 1].setValue(simple3RuleForCalculatingPercentage(simulationTime, personalizedTime));
        progressBar[queueIndex - 1].setString("Queue " + queueIndex + ": Task (" +
                currentTask.getId() + ", " + currentTask.getArrivalTime() + ", " +
                (currentTask.getServiceTime() - personalizedTime) + ") " +
                simple3RuleForCalculatingPercentage(simulationTime, personalizedTime)
                +"%");
    }
    public void completeProgress(int queueIndex)
    {
        progressBar[queueIndex - 1].setValue(100);
        progressBar[queueIndex - 1].setString("Queue " + queueIndex + ": 100%");
    }
    public void updateAverageWaitingTime(float value)
    {
        DecimalFormat df = new DecimalFormat("0.##");
        averageWaitingTime.setText("Average Waiting Time: " + df.format(value));
    }
    public void updateAverageServiceTime(float value)
    {
        DecimalFormat df = new DecimalFormat("0.##");
        averageServiceTime.setText("Average Service Time: " + df.format(value));
    }
    public void updatePeakHour(int value)
    {
        //setting the peak hour according to the value provided by the scheduler from the simulation manager
        peakHour.setText("Peak hour: " + value);
    }
}
