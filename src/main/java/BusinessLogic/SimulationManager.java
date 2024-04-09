package BusinessLogic;

import GUI.SimulationFrame;
import Model.Server;
import Model.Task;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class SimulationManager {
    private Scheduler scheduler;

    private boolean canAdd = false;
    public void setCanAdd(boolean canAdd) {
        this.canAdd = canAdd;
    }
    public SimulationFrame getCurrentFrame() {
        return frame;
    }
    private SimulationFrame frame;
    private final List<Task> tasks = new ArrayList<>();
    //private SelectionPolicy
    public SimulationManager(SimulationFrame frame) {
        this.scheduler = new Scheduler();
        this.frame = frame;
        generateRandomTasks();
    }
    private void generateRandomTasks()
    {
        int numberOfPeople = frame.getNumberClientsTextArea();
        int numberQueues = frame.getNumberQueuesAvailableTextArea();
        int simulationMaxInterval = frame.getSimulationIntervalTextArea();
        int arrivalStart = frame.getArrivalStartTextArea();
        int arrivalEnd = frame.getArrivalEndTextArea();
        int serviceStart = frame.getServiceStartTextArea();
        int serviceEnd = frame.getServiceEndTextArea();
        if(!checkValidityOfValues(numberOfPeople, numberQueues, simulationMaxInterval, arrivalStart,
                arrivalEnd, serviceStart, serviceEnd))
            return;
        Random random = new Random();
        Server server = new Server(numberOfPeople, numberQueues, simulationMaxInterval, this);
        int i;
        for (i = 0; i < numberQueues; i++) {
            int arrivalTime = random.nextInt(arrivalEnd - arrivalStart + 1) + arrivalStart;
            int serviceTime = random.nextInt(serviceEnd - serviceStart + 1) + serviceStart;
            Task task = new Task(i + 1, arrivalTime, serviceTime);
            tasks.add(task);
            server.addTask(task,0); //adding just enough at first
        }
        server.startProcessing(numberQueues); // <-- here
        numberOfPeople -= numberQueues;

        while(numberOfPeople > 0)
        {
            //I need to know when a thread has finished its execution
            if(canAdd)
            {
                int arrivalTime = random.nextInt(arrivalEnd - arrivalStart + 1) + arrivalStart;
                int serviceTime = random.nextInt(serviceEnd - serviceStart + 1) + serviceStart;
                Task task = new Task( i++ + 1, arrivalTime, serviceTime);
                server.addTask(task, server.getWaitingPeriod());
                server.startProcessing(1);
                numberOfPeople--;
                canAdd = false;
            }
        }
        System.out.println("You did it.\n");
        for(Task task: tasks)
            System.out.println(task.getId() + " " + task.getArrivalTime() + " " + task.getServiceTime());

    }
    private boolean checkValidityOfValues(int n1, int n2, int n3, int n4, int n5, int n6, int n7)
    {
        return n1 != -1 && n2 != -1 && n3 != -1 && n4 != -1 && n5 != -1 && n6 != -1 && n7 != -1;
    }
}
