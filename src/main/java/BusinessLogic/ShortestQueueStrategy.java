package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.ArrayList;
import java.util.List;


public class ShortestQueueStrategy implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task task) {
        if(!servers.isEmpty())
        {
            //check based on number of people
            removeCompletedTasks(servers, task.getArrivalTime());

            int smallestNumberOfWaiters = servers.get(0).getWaitingPeriodsForClients().size();
            int timeForSmallestNumberOfWaiters = servers.get(0).getWaitingPeriod().get();
            int index = 0;
            for(int i = 1; i < servers.size(); i++)
            {
                int numberOfPeopleCurrentServer = servers.get(i).getWaitingPeriodsForClients().size();
                if(numberOfPeopleCurrentServer < smallestNumberOfWaiters)
                {
                    timeForSmallestNumberOfWaiters = servers.get(i).getWaitingPeriod().get();
                    smallestNumberOfWaiters = numberOfPeopleCurrentServer;
                    index = i;
                }
            }
            timeUpdate(timeForSmallestNumberOfWaiters, servers, task, index);
            servers.get(index).addElementToArray(servers.get(index).getWaitingPeriod().get());//add waiting period for last client added
            servers.get(index).addTask(task); //add the client
        }
    }
    private void timeUpdate(int timeForSmallestNumberOfWaiters, List<Server> servers, Task task, int index)
    {
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
    }
    private void removeCompletedTasks(List<Server> servers, int arrivalTime)
    {
        for (Server server : servers) {
            ArrayList<Integer> arrayList = server.getWaitingPeriodsForClients();
            for (int j = arrayList.size() - 1; j >= 0; j--)
                if (arrayList.get(j) <= arrivalTime) {
                    server.deleteFirstIElementsFromArrayList(j);
                    break;
                }
        }
    }
}
