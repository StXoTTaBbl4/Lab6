package Program.Common.Command.Commands;

import Program.Common.Command.ICommand;
import Program.Common.DataClasses.Worker;

import java.util.LinkedList;

/**
 * Выводит в стандартный поток вывода все элементы коллекции в строковом представлении.
 */
public class ShowCommand implements ICommand {
    @Override
    public LinkedList<Worker> handle(String args, LinkedList<Worker> WorkersData) {

            if(WorkersData.size() == 0) {
                System.out.println("The collection is empty.");
                return WorkersData;
            }
            else {
                for (Worker worker : WorkersData) {
                    try {
                        System.out.println(worker.toString());
                    } catch (NullPointerException e) {
                        System.out.println("Worker with id: " + worker.getId() + "have incorrect data.\n");
                    }
                }
            }

        return WorkersData;
    }

    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String getHelp() {
        return "Returns a list of the elements of the collection.";
    }
}
