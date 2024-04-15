package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ShortestQueueStrategy implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task task) {
        if(!servers.isEmpty())
        {
            //check based on number of people
            int smallestNumberOfWaiters = servers.get(0).getNumberOfPeople();
            BlockingQueue<Task> taskQueueFirst = servers.get(0).getTasks();
            List<Task> taskListFirst = new ArrayList<>(taskQueueFirst);
            int timeForSmallestNumberOfWaiters = servers.get(0).getWaitingPeriod().get() + taskListFirst.getLast().getArrivalTime() + taskListFirst.getLast().getServiceTime();
            int index = 0;
            for(int i = 1; i < servers.size(); i++)
            {
                int numberOfPeopleCurrentServer = servers.get(i).getNumberOfPeople();
                if(numberOfPeopleCurrentServer < smallestNumberOfWaiters)
                {
                    BlockingQueue<Task> taskQueue = servers.get(i).getTasks();
                    List<Task> taskList = new ArrayList<>(taskQueue);
                    timeForSmallestNumberOfWaiters = servers.get(i).getWaitingPeriod().get() + taskList.getLast().getArrivalTime() + taskList.getLast().getServiceTime();
                    smallestNumberOfWaiters = numberOfPeopleCurrentServer;
                    index = i;
                }
            }
            //add task in server with index I
            int setArrivalTime = task.getArrivalTime();
            int arrivalTime = timeForSmallestNumberOfWaiters;
            if(arrivalTime > setArrivalTime)
                servers.get(index).addWaitingPeriod(arrivalTime - setArrivalTime);

            servers.get(index).addTask(task);
        }
    }
}
