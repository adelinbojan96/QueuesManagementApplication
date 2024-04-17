package BusinessLogic;

import GUI.QueueViewer;
import GUI.SimulationFrame;
import Model.Server;
import Model.Task;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SimulationManager{
    private Scheduler scheduler;
    private final SimulationFrame frame;
    private final QueueViewer viewFrame;
    private final List<Task> tasks = new ArrayList<>();
    private final boolean createTxtFile;
    public SimulationManager(SimulationFrame frame, QueueViewer queueViewer) {
        this.frame = frame;
        this.viewFrame = queueViewer;
        this.createTxtFile = frame.isCreateTxtFileSelected();
        generateRandomTasks();
    }
    private void generateRandomTasks()
    {
        int numberOfPeople = frame.getNumberClients();
        int numberQueues = frame.getNumberQueuesAvailable();
        int simulationMaxInterval = frame.getSimulationInterval();
        int arrivalStart = frame.getArrivalStart();
        int arrivalEnd = frame.getArrivalEnd();
        int serviceStart = frame.getServiceStart();
        int serviceEnd = frame.getServiceEnd();
        String strategyChooserString = frame.getStrategyChooser();

        if(!checkValidityOfValues(numberOfPeople, numberQueues, simulationMaxInterval, arrivalStart,
                arrivalEnd, serviceStart, serviceEnd))
        {
            JOptionPane.showMessageDialog(frame, "Input is not a valid.");
            return;
        }
        if(numberOfPeople < numberQueues) {
            numberQueues = numberOfPeople;
        }

        Server[] servers = new Server[numberQueues];
        generateAndSortTasks(numberOfPeople, arrivalStart, arrivalEnd, serviceStart, serviceEnd);
        addClientsToQueuesAccordingly(servers, simulationMaxInterval, numberQueues, numberOfPeople, strategyChooserString);
        startProcessing(servers, numberQueues);
        //UI updates
        viewFrame.updateAverageWaitingTime(scheduler.calculateAverageWaitingTime(numberOfPeople));
        viewFrame.updateAverageServiceTime(scheduler.calculateAverageServiceTime(tasks, numberOfPeople));
    }
    public SimulationFrame getFrame() {
        return frame;
    }
    public Scheduler getScheduler() {
        return scheduler;
    }
    private void generateAndSortTasks(int numberOfPeople, int arrivalStart, int arrivalEnd, int serviceStart, int serviceEnd)
    {
        Task[] task = new Task[numberOfPeople];
        //generate it
        for (int i = 0; i < numberOfPeople; i++)
            task[i] = generateTask(i, arrivalStart, arrivalEnd, serviceStart, serviceEnd);
        //sort it
        for(int i = 0; i < numberOfPeople - 1; i++)
            for(int j = i + 1; j < numberOfPeople; j++)
                if(task[i].getArrivalTime() > task[j].getArrivalTime())
                {
                    Task newTask = task[j];
                    task[j] = task[i];
                    task[i] = newTask;
                }
        tasks.addAll(Arrays.asList(task).subList(0, numberOfPeople)); //add task[] in tasks list
    }
    private Task generateTask(int index, int arrivalStart, int arrivalEnd, int serviceStart, int serviceEnd)
    {
        //generate random tasks according to variables from frame
        Random random = new Random();

        int arrivalTime = random.nextInt(arrivalEnd - arrivalStart + 1) + arrivalStart;
        int serviceTime = random.nextInt(serviceEnd - serviceStart + 1) + serviceStart;
        return new Task(index + 1, arrivalTime, serviceTime);
    }
    private boolean checkValidityOfValues(int n1, int n2, int n3, int n4, int n5, int n6, int n7)
    {
        if(n4 > n5)
            return false;
        if(n6 > n7)
            return false;
        return (n1 > 0 && n2 > 0 && n3 > 0 && n4 > 0 && n6 > 0);
    }
    private void addClientsToQueuesAccordingly(Server[] servers, int simulationMaxInterval, int numberOfQueues, int numberOfPeople, String strategyChooserString )
    {
        int i = 0;
        for(; i < numberOfQueues; i++)
        {
            servers[i] = new Server(simulationMaxInterval, this, createTxtFile, viewFrame, i + 1);
            Task newTask = tasks.get(i);
            servers[i].addTask(newTask);
            servers[i].setWaitingPeriod(newTask.getArrivalTime() + newTask.getServiceTime());
            servers[i].addServerWaitingTime(newTask.getServiceTime());
            servers[i].addElementToArray(newTask.getArrivalTime() + newTask.getServiceTime());
        }
        scheduler = new Scheduler(servers, strategyChooserString);
        scheduler.dispatchTasks(i, numberOfPeople, tasks);
    }
    public void startProcessing(Server[] servers, int threadsToStart) {
        Thread[] workerThreads = new Thread[threadsToStart];
        for (int i = 0; i < threadsToStart; i++) {

            workerThreads[i] = new Thread(servers[i]);
        }
        for (int i = 0; i < threadsToStart; i++) {
            workerThreads[i].start();
            //number of people is decremented in the run method
        }

    }
}

