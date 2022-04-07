package Program.Common.Command.Commands;

import Program.Common.Command.ICommand;
import Program.Common.DataClasses.Worker;

import java.util.LinkedList;

/**
 * Выводит элементы, значение поля salary которых больше заданного.
 */
public class FilterGtsCommand implements ICommand {
    @Override
    public LinkedList<Worker> handle(String args, LinkedList<Worker> WorkersData) {

        Float salary = null;
        try {
            salary = Float.parseFloat(args);
        }
            catch (NumberFormatException e){
            System.out.println("Invalid data type. Example: 1500.99.");
        }

        for (Worker w : WorkersData) {
            try {
                if(w.getSalary() > salary)
                    System.out.println(w);
                }
            catch (NullPointerException e){
                if(w.getSalary() == null)
                System.out.printf("The salary field is not set for id: %s.\n", w.getId());
            }
        }

        return WorkersData;
    }

    @Override
    public String getName() {
        return "filter_greater_than_salary";
    }

    @Override
    public String getHelp() {
        return "Displays elements whose salary field value is greater than the specified value.";
    }
}
