package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;

public class Scheduler {
    private List<Server> servers;

    private Strategy strategy;
    private StrategyType strategyType;

    //should theoretically send task to servers according to desired strategy
    private enum StrategyType {
        STRATEGY_QUEUE,
        STRATEGY_TIME;
    }
    public Scheduler(Server[] servers, String strategy)
    {
        this.servers = List.of(servers);
        this.strategyType = (strategy.equals("Shortest Time")) ? StrategyType.STRATEGY_TIME : StrategyType.STRATEGY_QUEUE;
    }
    public List<Server> getServers() {
        return servers;
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

}
