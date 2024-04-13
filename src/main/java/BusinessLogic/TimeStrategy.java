package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;

public class TimeStrategy implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task task) {
        if(!servers.isEmpty())
        {
            int smallestTime = servers.get(0).getWaitingPeriod().get();
            int index = 0;
            for(int i = 1; i < servers.size(); i++)
            {
                int waitingPeriodCurrentServer = servers.get(i).getWaitingPeriod().get();
                if(waitingPeriodCurrentServer < smallestTime)
                {
                    smallestTime = waitingPeriodCurrentServer;
                    index = i;
                }
            }
            servers.get(index).addTask(task);
        }
    }
}
