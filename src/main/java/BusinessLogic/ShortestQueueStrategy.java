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
            int index = 0;
            for(int i = 1; i < servers.size(); i++)
            {
                int numberOfPeopleCurrentServer = servers.get(i).getNumberOfPeople();
                if(numberOfPeopleCurrentServer < smallestNumberOfWaiters)
                {
                    smallestNumberOfWaiters = numberOfPeopleCurrentServer;
                    index = i;
                }
            }
            //add task in server with index i
            servers.get(index).addTask(task);
        }
    }

}
