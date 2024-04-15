package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class Scheduler {
    private List<Server> servers;
    private Strategy strategy;
    private StrategyType strategyType;
    private int maxValuePeak;
    private int maxHourPeak;

    //should theoretically send task to servers according to desired strategy
    private enum StrategyType {
        STRATEGY_QUEUE,
        STRATEGY_TIME;
    }
    public Scheduler(Server[] servers, String strategy)
    {
        this.maxHourPeak = 0;
        this.maxValuePeak = 0;
        this.servers = List.of(servers);
        this.strategyType = (strategy.equals("Shortest Time")) ? StrategyType.STRATEGY_TIME : StrategyType.STRATEGY_QUEUE;
    }
    public void dispatchTasks(int currentIndex, int numberOfPeople, List<Task> tasks)
    {
        if(strategyType == StrategyType.STRATEGY_QUEUE)
        {
            //this is shortestQueue, related to people
            ShortestQueueStrategy queueStrategy = new ShortestQueueStrategy();
            for(int i = currentIndex; i < numberOfPeople; i++)
                queueStrategy.addTask(servers, tasks.get(i));
        }
        else
        {
            TimeStrategy timeStrategy = new TimeStrategy();
            for(int i = currentIndex; i < numberOfPeople; i++)
                timeStrategy.addTask(servers, tasks.get(i));
        }
    }
    public float calculateAverageWaitingTime(List<Task> tasks, int numberOfPeople)
    {
        int totalServiceTime = 0;
        int totalWaitingTime = 0;

        for(Task task: tasks)
            totalServiceTime += task.getServiceTime();

        for (Server server : servers) {
            int waitingPeriod = server.getWaitingPeriod().get();
            totalWaitingTime += waitingPeriod / server.getNumberOfPeople();
        }
        System.out.println(totalServiceTime);
        if(numberOfPeople > 0)
            return (float) (totalWaitingTime)/servers.size() + (float) (totalServiceTime) / numberOfPeople;
        else
            return 0;
    }
    public float calculateAverageServiceTime(List<Task> tasks, int numberOfPeople)
    {
        int totalServiceTime = 0;
        for(Task task: tasks)
            totalServiceTime += task.getServiceTime();

        if(numberOfPeople > 0)
            return (float) totalServiceTime/numberOfPeople;
        else
            return 0;
    }
    public int updatePeakHour(int time)
    {
        int counter = 0;
        for (Server server: servers) {
            if(server.isOn())
                counter++;
        }
        if(maxValuePeak < counter)
        {
            maxHourPeak = time;
            maxValuePeak = counter;
            return time;
        }
        return -1;
    }
}
