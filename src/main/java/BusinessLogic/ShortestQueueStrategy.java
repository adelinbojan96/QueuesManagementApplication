package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;


public class ShortestQueueStrategy implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task task) {
        if(!servers.isEmpty())
        {
            //check based on number of people
            int smallestNumberOfWaiters = servers.get(0).getNumberOfPeople();
            int timeForSmallestNumberOfWaiters = servers.get(0).getWaitingPeriod().get();
            int index = 0;
            for(int i = 1; i < servers.size(); i++)
            {
                int numberOfPeopleCurrentServer = servers.get(i).getNumberOfPeople();
                if(numberOfPeopleCurrentServer < smallestNumberOfWaiters)
                {
                    timeForSmallestNumberOfWaiters = servers.get(i).getWaitingPeriod().get();
                    smallestNumberOfWaiters = numberOfPeopleCurrentServer;
                    index = i;
                }
            }
            int setArrivalTime = task.getArrivalTime();
            if(timeForSmallestNumberOfWaiters < setArrivalTime)
            {
                servers.get(index).addWaitingPeriod(task.getServiceTime() + (setArrivalTime-timeForSmallestNumberOfWaiters));
                servers.get(index).addServerWaitingTime(task.getServiceTime());
            }
            else
            {
                servers.get(index).addWaitingPeriod(task.getServiceTime());
                servers.get(index).addServerWaitingTime(timeForSmallestNumberOfWaiters - setArrivalTime + task.getServiceTime());
            }
            servers.get(index).addTask(task);
        }
    }
}
