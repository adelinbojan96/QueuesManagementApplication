package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class TimeStrategy implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task task) {
        if(!servers.isEmpty())
        {
            BlockingQueue<Task> taskQueueFirst = servers.get(0).getTasks();
            List<Task> taskListFirst = new ArrayList<>(taskQueueFirst);
            int smallestTime = servers.get(0).getWaitingPeriod().get() + taskListFirst.getLast().getArrivalTime() + taskListFirst.getLast().getServiceTime();
            int index = 0;
            for(int i = 1; i < servers.size(); i++)
            {
                BlockingQueue<Task> taskQueue = servers.get(i).getTasks();
                List<Task> taskList = new ArrayList<>(taskQueue);
                int waitingPeriodCurrentServer = servers.get(i).getWaitingPeriod().get() + taskList.getLast().getArrivalTime() + taskList.getLast().getServiceTime();
                if(waitingPeriodCurrentServer < smallestTime)
                {
                    smallestTime = waitingPeriodCurrentServer;
                    index = i;
                }
            }
            int setArrivalTime = task.getArrivalTime();
            int arrivalTime = smallestTime;
            if(arrivalTime > setArrivalTime)
                servers.get(index).addWaitingPeriod(arrivalTime - setArrivalTime); // if arrivalTime is greater
            servers.get(index).addTask(task);
        }
    }
}
