package Model;

import BusinessLogic.SimulationManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;

    public int getRemoved() {
        int value = removed;
        if(value != 0)
        {
            removed = 0;
            return value;
        }
        else
            return 0;
    }
    private int removed = 0;
    private int numberOfThreads;

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    private int numberOfPeople;
    private int simulationMaxInterval;
    private int currentIndex = 0;
    private SimulationManager simulationManager;
    private AtomicInteger timeSet;
    private final Set<Integer> printedTimes;

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public Server(int numberOfPeople, int numberOfThreads, int simulationMaxInterval, SimulationManager simulationManager)
    {
        this.numberOfThreads = numberOfThreads;
        this.numberOfPeople = numberOfPeople;
        this.tasks = new ArrayBlockingQueue<>(Math.min(numberOfPeople, numberOfThreads));
        this.simulationMaxInterval = simulationMaxInterval;
        this.waitingPeriod = new AtomicInteger(0);
        this.timeSet = new AtomicInteger(0);
        this.simulationManager = simulationManager;
        this.printedTimes = new HashSet<>();
    }
    public AtomicInteger getTimeSet() {
        return timeSet;
    }

    private void addToCurrentQueue(List<Task> tasksSimulation, int numberOfThreads)
    {
        int initialNumberOfThreads = numberOfThreads;
        int numberOfElementsSucceededInAdding = 0;
        //add current tasks
        for(int i = currentIndex; i < tasksSimulation.size() && numberOfThreads > 0; i++, numberOfThreads--)
        {
            tasks.add(tasksSimulation.get(i));
            numberOfElementsSucceededInAdding++;
        }

        currentIndex += initialNumberOfThreads;
        removed = initialNumberOfThreads - numberOfElementsSucceededInAdding;
    }
    public void addTask(Task task)
    {
        //add in list for one queue
        tasks.add(task);
    }
    public void startProcessing(int threadsToStart) {
        //todo: this can be in simulation manager
        //Threads to start = available queues
        addToCurrentQueue(simulationManager.getTasks(), threadsToStart); //add to current queue the threads that I need to start
        Thread[] workerThreads = new Thread[threadsToStart];
        for (int i = 0; i < threadsToStart; i++) {
            workerThreads[i] = new Thread(this);
        }
        for (int i = 0; i < threadsToStart; i++) {
            workerThreads[i].start();
            //number of people is decremented in the run method
        }
    }
    private synchronized void printAndSet(int simulationTimeThread)
    {
        if (!printedTimes.contains(simulationTimeThread)) {
            System.out.println("Time: " + simulationTimeThread);
            printedTimes.add(simulationTimeThread);
        }
    }
    private void processTask(int simulationTimeThread, int currentArrivalTime, boolean arrived, Task task, int currentServiceTime, int personalizedServiceTime, boolean end, boolean wasInWhile)
    {
        while (numberOfPeople > 0 && simulationTimeThread <= simulationMaxInterval && !end) {
            try {
                Thread.sleep(1000);
                printAndSet(simulationTimeThread);
                if(simulationTimeThread >= currentArrivalTime && !arrived)
                    arrived = true;
                if(arrived)
                {
                    System.out.println("("+task.getId() + ", " + (currentServiceTime - personalizedServiceTime) + ")");
                    personalizedServiceTime++;
                }
                if(personalizedServiceTime >= currentServiceTime)
                {
                    end = true;
                    numberOfPeople--;
                    removed++;
                }
                wasInWhile = true;
                simulationTimeThread++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(numberOfPeople > 0 && wasInWhile)
        {
            timeSet.set(simulationTimeThread);
            startProcessing(1);
        }
    }
    @Override
    public void run() {
        Task task;
        try {
            task = tasks.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int currentServiceTime = task.getServiceTime();
        int currentArrivalTime = task.getArrivalTime();
        int simulationTimeThread = timeSet.get();
        int personalizedServiceTime = 0;
        boolean arrived = false;
        boolean end = false;
        boolean wasInWhile = false;
        processTask(simulationTimeThread, currentArrivalTime, arrived, task, currentServiceTime, personalizedServiceTime, end, wasInWhile);
    }
}
