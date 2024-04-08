package BusinessLogic;

import GUI.SimulationFrame;
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
    private static int index = 1;
    private final List<Task> tasks = new ArrayList<>();
    //private SelectionPolicy
    public SimulationManager(SimulationFrame frame) {
        this.scheduler = new Scheduler();
        this.frame = frame;
        generateRandomTasks();
    }
    private void generateRandomTasks()
    {
        int numberQueues = frame.getNumberQueuesAvailableTextArea();
        int simulationInterval = frame.getSimulationIntervalTextArea();
        int arrivalStart = frame.getArrivalStartTextArea();
        int arrivalEnd = frame.getArrivalEndTextArea();
        int serviceStart = frame.getServiceStartTextArea();
        int serviceEnd = frame.getServiceEndTextArea();

        Random random = new Random();

        for (int i = 0; i < numberQueues; i++) {
            int arrivalTime = random.nextInt(arrivalEnd - arrivalStart + 1) + arrivalStart;
            int serviceTime = random.nextInt(serviceEnd - serviceStart + 1) + serviceStart;
            Task task = new Task(index++, arrivalTime, serviceTime);
            tasks.add(task);
        }
        System.out.println("You did it.\n");
        for(Task task: tasks)
            System.out.println(task.getId());
    }
}
