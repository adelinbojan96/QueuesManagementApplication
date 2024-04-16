package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;

public class Scheduler {
    private final List<Server> servers;
    private final StrategyType strategyType;
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
    public float calculateAverageWaitingTime(int numberOfPeople)
    {
        int totalWaitingTime = 0;

        for (Server server : servers) {
            int waitingPeriod = server.getServerWaitingTime().get();
            totalWaitingTime += waitingPeriod;
        }
        if(numberOfPeople != 0)
            return (float) (totalWaitingTime)/(numberOfPeople);
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
            return maxHourPeak;
        }
        return -1;
    }
}
