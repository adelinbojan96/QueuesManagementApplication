package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;

public class Scheduler {
    private List<Server> servers;
    private Strategy strategy;
    //should theoretically send task to servers according to desired strategy
    public void chooseStrategy(boolean time_queue)
    {

    }
    public void dispatchTask(Task task)
    {

    }

}
