package Model;

import BusinessLogic.SimulationManager;
import GUI.QueueViewer;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private final LinkedBlockingQueue<Task> tasks;
    private final AtomicInteger waitingPeriod;
    private final AtomicInteger serverWaitingTime;
    private boolean on;
    public int getNumberOfPeople() {
        return numberOfPeople;
    }
    private int numberOfPeople;
    private final int simulationMaxInterval;
    private final SimulationManager simulationManager;
    private final QueueViewer queueViewer;
    private final AtomicInteger timeSet;
    private static List<Integer> printedTimes;
    private final int serverIndex;
    private final boolean displayTxtFile;
    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public AtomicInteger getServerWaitingTime() {
        return serverWaitingTime;
    }
    public void addServerWaitingTime(int time)
    {
        this.serverWaitingTime.addAndGet(time);
    }
    public void addWaitingPeriod(int waitingPeriod) {
        this.waitingPeriod.set((this.waitingPeriod.get() + waitingPeriod));
    }
    public void setWaitingPeriod(int waitingPeriod)
    {
        this.waitingPeriod.set(waitingPeriod);
    }
    public Server(int simulationMaxInterval, SimulationManager simulationManager, boolean displayTxtFile, QueueViewer queueViewer, int serverIndex)
    {
        this.numberOfPeople = 0;
        this.tasks = new LinkedBlockingQueue<>();
        this.simulationMaxInterval = simulationMaxInterval;
        this.waitingPeriod = new AtomicInteger(0);
        this.timeSet = new AtomicInteger(0);
        this.serverWaitingTime = new AtomicInteger(0);
        this.simulationManager = simulationManager;
        this.displayTxtFile = displayTxtFile;
        this.queueViewer = queueViewer;

        printedTimes = Collections.synchronizedList(new ArrayList<>());
        this.serverIndex = serverIndex;
        this.on = false;
    }
    private void continueProcessing()
    {
        Thread workerThread = new Thread(this);
        workerThread.start();
    }
    public boolean isOn() {
        return on;
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
                if(displayTxtFile)
                {
                    try {
                        simulationManager.getFrame().writeToFile("Time: " + simulationTimeThread);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                printedTimes.add(simulationTimeThread);
                queueViewer.updateTimingText(simulationTimeThread);
                int time = simulationManager.getScheduler().updatePeakHour(simulationTimeThread);
                if(time!=-1)
                    queueViewer.updatePeakHour(time);
            }
        }
    }
    private void continueOrEnd(boolean wasInWhile, int simulationTimeThread)
    {
        if(numberOfPeople > 0 && wasInWhile)
        {
            timeSet.set(simulationTimeThread);
            continueProcessing();
        }
        else if(simulationTimeThread < simulationMaxInterval)
        {
            //set task completed
            try{
                Thread.sleep(1000);
                printAndSet(simulationTimeThread);
                queueViewer.completeProgress(serverIndex);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void processTask(int simulationTimeThread, int currentArrivalTime, boolean arrived, Task task, int currentServiceTime, int personalizedServiceTime, boolean end, boolean wasInWhile)
    {
        while (numberOfPeople > 0 && simulationTimeThread <= simulationMaxInterval && !end) {
            try {
                Thread.sleep(1000);
                printAndSet(simulationTimeThread);
                queueViewer.updateProgress(serverIndex, currentServiceTime, personalizedServiceTime, task);
                if(simulationTimeThread >= currentArrivalTime && !arrived)
                {
                    arrived = true;
                    on = true;
                }
                if(arrived)
                {
                    try {
                        simulationManager.getFrame().writeToFile("("+task.getId() + ", " + currentArrivalTime + ", " + (currentServiceTime - personalizedServiceTime) + ") in queue: " + serverIndex);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    personalizedServiceTime++;
                }
                if(personalizedServiceTime >= currentServiceTime)
                {
                    on = false;
                    end = true;
                    numberOfPeople--;
                }
                wasInWhile = true;
                simulationTimeThread++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        continueOrEnd(wasInWhile, simulationTimeThread);
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
