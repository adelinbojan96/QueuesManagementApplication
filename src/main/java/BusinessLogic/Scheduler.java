package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;

public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;
    private Strategy strategy;
    public void changeStrategy()//SelectionPolicy selectionPolicy
    {

    }
    public void dispatchTask(Task task)
    {

    }

}