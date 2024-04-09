package Model;

import BusinessLogic.SimulationManager;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private int numberOfThreads;
    private int numberOfPeople;
    private int simulationMaxInterval;
    private int simulationTime;
    private SimulationManager simulationManager;
    public Server(int numberOfPeople, int numberOfThreads, int simulationMaxInterval, SimulationManager simulationManager)
    {
        this.numberOfThreads = numberOfThreads;
        this.numberOfPeople = numberOfPeople;
        this.tasks = new ArrayBlockingQueue<>(Math.min(numberOfPeople, numberOfThreads));
        this.simulationMaxInterval = simulationMaxInterval;
        this.waitingPeriod = new AtomicInteger(0);
        this.simulationTime = 0;
        this.simulationManager = simulationManager;
    }

    public void addTask(Task newTask, int startTime) {

        simulationTime = startTime;
        tasks.add(newTask);
    }
    public void startProcessing(int numberOfThreads) {
        Thread[] workerThreads = new Thread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            workerThreads[i] = new Thread(this);
        }
        for (int i = 0; i < numberOfThreads; i++) {
            workerThreads[i].start();
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
        int simulationTimeThread = simulationTime;
        boolean removed = false;
        System.out.println("Task " + task.getId() + " begin at time " + task.getArrivalTime() + " end at " + (task.getArrivalTime() + task.getServiceTime()));
        System.out.println("Check initial simulationTime: " + simulationTimeThread);
        System.out.println("Check if tasks are empty: " + tasks.isEmpty());
        while (simulationTimeThread <= simulationMaxInterval) {
            try {
                Thread.sleep(1000);
                System.out.println("time: " + simulationTimeThread);
                if(simulationTimeThread == task.getArrivalTime())
                {
                    //if simulation time is at least the minimum arrival time from task
                    waitingPeriod.addAndGet(simulationTimeThread);
                    System.out.println("Task " + task.getId() + " begin at time " + simulationTimeThread);
                    System.out.println("Current waiting period: " + waitingPeriod.get());
                }
                else if(simulationTimeThread == task.getServiceTime() + task.getArrivalTime())
                {
                    waitingPeriod.addAndGet(task.getArrivalTime());
                    System.out.println("Task " + task.getId() + " ended at " + simulationTimeThread);
                    numberOfPeople--;
                    System.out.println("Remaining people: " + numberOfPeople);
                    removed = tasks.remove(task);
                }
                simulationTimeThread++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(removed)
            {
                //send information to SimulationManager
                simulationManager.setCanAdd(true);
            }
        }
    }
    public int getWaitingPeriod() {
        return waitingPeriod.get();
    }
}
