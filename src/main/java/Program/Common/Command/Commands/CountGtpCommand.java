package Program.Common.Command.Commands;

import Program.Common.Command.ICommand;
import Program.Common.DataClasses.Person;
import Program.Common.DataClasses.Worker;

import java.util.LinkedList;

/**
 * Выводит количество элементов, значение поля person которых больше заданного.
 */
public class CountGtpCommand implements ICommand {
    @Override
    public LinkedList<Worker> handle(String args, LinkedList<Worker> WorkersData) {

        AddCommand addCommand = new AddCommand();
        Person person = new Person();

        String[] userData = args.replaceAll(",","").split(" ");

        if(userData.length < 5){
            System.out.println("The person fields are incorrect, should be 5.\n" +
                                "Example: 2002-02-02 12:20 180 75 passID.");
            return WorkersData;
        }
        else {
            person = addCommand.createNewPerson(userData[0], userData[1], userData[2], userData[3], userData[4], person);
        }

        int k = 0;
        if(person == null) {
            System.out.println("person is null, cannot be counted.");
            return WorkersData;
        }
        else {
            for (Worker w : WorkersData) {
                try {
                    if (compare(w.getPerson(), person) > 0)
                        k++;
                } catch (NullPointerException e) {
                    System.out.printf("The person field is not set for id: %s.\n", w.getId());
                }
            }
        }

        System.out.println(k);
        return WorkersData;
    }

    @Override
    public String getName() {
        return "count_greater_than_person";
    }

    @Override
    public String getHelp() {
        return "Returns the number of elements whose person field value is greater than the specified value.";
    }

    private int compare(Person a, Person b){
        int aScore = 0;
        int bScore = 0;

        try {
            aScore = (int) (a.getHeight() + a.getWeight());
            bScore = (int) (b.getHeight() + b.getWeight()) ;
        }catch (NullPointerException e){
            System.out.println("Data error.");
        }
        return aScore - bScore;
    }

}
