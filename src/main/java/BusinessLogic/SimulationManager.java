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
    public SimulationFrame getCurrentFrame() {
        return frame;
    }
    private SimulationFrame frame;

    public List<Task> getTasks() {
        return tasks;
    }

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

        numberOfPeople = 4;
        numberQueues = 2;
        Server server = new Server(numberOfPeople, numberQueues, 60, this);
        /*  Randomly generated
        int i;
        for (i = 0; i < numberOfPeople; i++) {
            Task task = generateTask(i + 1);
            tasks.add(task);
        }
         */
        //HardCoded
        Task task1 = new Task( 1, 2, 2);
        tasks.add(task1);
        Task task2 = new Task(2, 3, 3);
        tasks.add(task2);
        Task task3 = new Task(3, 4, 3);
        tasks.add(task3);
        Task task4 = new Task(4, 10, 2);
        tasks.add(task4);
        server.startProcessing(numberQueues);

        System.out.println("You did it.\n");
        for(Task task: tasks)
            System.out.println(task.getId() + " " + task.getArrivalTime() + " " + task.getServiceTime());

    }
    public Task generateTask(int index)
    {
        int arrivalStart = frame.getArrivalStartTextArea();
        int arrivalEnd = frame.getArrivalEndTextArea();
        int serviceStart = frame.getServiceStartTextArea();
        int serviceEnd = frame.getServiceEndTextArea();

        Random random = new Random();

        int arrivalTime = random.nextInt(arrivalEnd - arrivalStart + 1) + arrivalStart;
        int serviceTime = random.nextInt(serviceEnd - serviceStart + 1) + serviceStart;
        return new Task(index + 1, arrivalTime, serviceTime);
    }
    private boolean checkValidityOfValues(int n1, int n2, int n3, int n4, int n5, int n6, int n7)
    {
        return n1 != -1 && n2 != -1 && n3 != -1 && n4 != -1 && n5 != -1 && n6 != -1 && n7 != -1;
    }
}
