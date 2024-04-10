package Model;

import BusinessLogic.SimulationManager;

import java.util.List;
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
    private int numberOfPeople;
    private int simulationMaxInterval;
    private int currentIndex = 0;
    private SimulationManager simulationManager;
    public Server(int numberOfPeople, int numberOfThreads, int simulationMaxInterval, SimulationManager simulationManager)
    {
        this.numberOfThreads = numberOfThreads;
        this.numberOfPeople = numberOfPeople;
        this.tasks = new ArrayBlockingQueue<>(Math.min(numberOfPeople, numberOfThreads));
        this.simulationMaxInterval = simulationMaxInterval;
        this.waitingPeriod = new AtomicInteger(0);
        this.simulationManager = simulationManager;
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
    public void startProcessing(int threadsToStart) {
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
        int simulationTimeThread = waitingPeriod.get();
        int personalizedServiceTime = 0;
        boolean arrived = false;
        boolean end = false;
        boolean wasInWhile = false;
        System.out.println("Task " + task.getId() + " begin at time " + task.getArrivalTime() + " end at " + (task.getArrivalTime() + task.getServiceTime()));
        System.out.println("Check initial simulationTime: " + simulationTimeThread);
        System.out.println("Check if tasks are empty: " + tasks.isEmpty());
        while (numberOfPeople > 0 && simulationTimeThread <= simulationMaxInterval && !end) {
            try {
                Thread.sleep(1000);
                System.out.println("time: " + simulationTimeThread);
                if(simulationTimeThread >= currentArrivalTime && !arrived)
                {
                    //if simulation time is at least the minimum arrival time from task
                    arrived = true;
                    System.out.println("Task " + task.getId() + " begin at time " + simulationTimeThread);
                    System.out.println("Current waiting period: " + waitingPeriod.get());
                }

                else if(personalizedServiceTime >= currentServiceTime)
                {
                    System.out.println("task when it ends is: " + task.getServiceTime());
                    end = true;
                    System.out.println("Task " + task.getId() + " ended at " + simulationTimeThread);
                    numberOfPeople--;
                    System.out.println("Remaining people: " + numberOfPeople);
                    removed++;
                }
                wasInWhile = true;
                simulationTimeThread++;
                if(arrived)
                    personalizedServiceTime++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(numberOfPeople > 0 && wasInWhile)
        {
            waitingPeriod.set(simulationTimeThread - 1);
            startProcessing(1);
            //waitingPeriod.addAndGet(-simulationTimeThread);
        }
    }
}
