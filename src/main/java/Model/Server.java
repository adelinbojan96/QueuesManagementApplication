package Model;

import BusinessLogic.SimulationManager;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private LinkedBlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;

    public LinkedBlockingQueue<Task> getTasks() {
        return tasks;
    }
    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    private int numberOfPeople;

    private int simulationMaxInterval;
    private int currentIndex = 0;
    private SimulationManager simulationManager;
    private AtomicInteger timeSet;
    private static List<Integer> printedTimes;
    private final int serverIndex;
    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }
    public void addWaitingPeriod(int waitingPeriod) {
        this.waitingPeriod.addAndGet(waitingPeriod);
    }
    public void setWaitingPeriod(int waitingPeriod)
    {
        this.waitingPeriod.set(waitingPeriod);
    }
    public Server(int simulationMaxInterval, SimulationManager simulationManager, int serverIndex)
    {
        this.numberOfPeople = 0;
        this.tasks = new LinkedBlockingQueue<>();
        this.simulationMaxInterval = simulationMaxInterval;
        this.waitingPeriod = new AtomicInteger(0);
        this.timeSet = new AtomicInteger(0);
        this.simulationManager = simulationManager;
        printedTimes = Collections.synchronizedList(new ArrayList<>());;
        this.serverIndex = serverIndex;
    }
    public AtomicInteger getTimeSet() {
        return timeSet;
    }
    private void continueProcessing()
    {
        Thread workerThread = new Thread(this);
        workerThread.start();
    }
    public void addTask(Task task)
    {
        //add in list for one queue
        tasks.add(task);
        numberOfPeople++;
    }
    private void printAndSet(int simulationTimeThread) {
        synchronized (printedTimes) {
            if (!printedTimes.contains(simulationTimeThread)) {
                System.out.println("Time: " + simulationTimeThread);
                printedTimes.add(simulationTimeThread);
            }
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
                    System.out.println("("+task.getId() + ", " + (currentServiceTime - personalizedServiceTime) + ") with " + serverIndex);
                    personalizedServiceTime++;
                }
                if(personalizedServiceTime >= currentServiceTime)
                {
                    end = true;
                    numberOfPeople--;
                    //removed++;
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
            continueProcessing();
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
