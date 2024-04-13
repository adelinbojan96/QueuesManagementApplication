package BusinessLogic;

import GUI.SimulationFrame;
import Model.Server;
import Model.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulationManager{
    private Scheduler scheduler;
    private SimulationFrame frame;
    public List<Task> getTasks() {
        return tasks;
    }
    private final List<Task> tasks = new ArrayList<>();
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

        Server server = new Server(4, 2, 60, this);
        generateAndSortTasks(numberOfPeople, arrivalStart, arrivalEnd, serviceStart, serviceEnd);

        Task task1 = new Task(1, 2, 2);
        Task task2 = new Task(2, 3, 3);
        Task task3 = new Task(3, 4, 3);
        Task task4 = new Task(4, 10, 2);
        tasks.add(task1); tasks.add(task2); tasks.add(task3); tasks.add(task4);

        server.startProcessing(numberQueues);
        System.out.println("You did it.\n");
        for(Task task: tasks)
            System.out.println(task.getId() + " " + task.getArrivalTime() + " " + task.getServiceTime());

    }
    private void generateAndSortTasks(int numberOfPeople, int arrivalStart, int arrivalEnd, int serviceStart, int serviceEnd)
    {
        Task[] task = new Task[numberOfPeople];
        //generate it
        for (int i = 0; i < numberOfPeople; i++)
            task[i] = generateTask(i + 1, arrivalStart, arrivalEnd, serviceStart, serviceEnd);
        //sort it
        for(int i = 0; i < numberOfPeople - 1; i++)
            for(int j = i + 1; j < numberOfPeople; j++)
                if(task[i].getArrivalTime() > task[j].getArrivalTime())
                {
                    Task newTask = task[j];
                    task[j] = task[i];
                    task[i] = newTask;
                }
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
        return n1 != -1 && n2 != -1 && n3 != -1 && n4 != -1 && n5 != -1 && n6 != -1 && n7 != -1;
    }


}

