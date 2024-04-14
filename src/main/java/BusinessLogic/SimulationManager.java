package BusinessLogic;

import GUI.SimulationFrame;
import Model.Server;
import Model.Task;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SimulationManager{
    private Scheduler scheduler;
    private SimulationFrame frame;
    private final List<Task> tasks = new ArrayList<>();
    public SimulationManager(SimulationFrame frame) {
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
        String strategyChooserString = frame.getStrategyChooser();
        if(!checkValidityOfValues(numberOfPeople, numberQueues, simulationMaxInterval, arrivalStart,
                arrivalEnd, serviceStart, serviceEnd))
            return;

        Server[] servers = new Server[numberQueues];
        //generateAndSortTasks(numberOfPeople, arrivalStart, arrivalEnd, serviceStart, serviceEnd);
        testing();
        addClientsToQueuesAccordingly(servers, simulationMaxInterval, numberQueues, numberOfPeople, strategyChooserString);
        startProcessing(servers, numberQueues);
        System.out.println("Initial tasks:\n");
        for(Task task: tasks)
            System.out.println("(" + task.getId() + ", " + task.getArrivalTime() + ", " + task.getServiceTime() + ")");
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
        return n1 != -1 && n2 != -1 && n3 != -1 && n4 != -1 && n5 != -1 && n6 != -1 && n7 != -1;
    }
    private void testing()
    {
        Task[] task = new Task[7];
        task[0] = new Task(2, 2, 2);
        task[1] = new Task(6, 2, 3);
        task[2] = new Task(7, 2, 3);
        task[3] = new Task(8, 2, 3);
        task[4] = new Task(5, 4, 2);
        task[5] = new Task(3, 5, 3);
        task[6] = new Task(4, 5, 1);
        for(int i = 0; i < 7 - 1; i++)
            for(int j = i + 1; j < 7; j++)
                if(task[i].getArrivalTime() > task[j].getArrivalTime())
                {
                    Task newTask = task[j];
                    task[j] = task[i];
                    task[i] = newTask;
                }
        tasks.addAll(Arrays.asList(task).subList(0, 7));
    }
    private void addClientsToQueuesAccordingly(Server[] servers, int simulationMaxInterval, int numberOfQueues, int numberOfPeople, String strategyChooserString )
    {
        int i = 0;
        for(; i < numberOfQueues; i++)
        {
            System.out.println(tasks.get(i).getId() + " has " + (i + 1));
            servers[i] = new Server(simulationMaxInterval, this, i + 1);
            Task newTask = tasks.get(i);
            servers[i].addTask(newTask);
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

