package Program.Common.Command.Commands;

import Program.Common.Command.ICommand;
import Program.Common.DataClasses.Coordinates;
import Program.Common.DataClasses.Person;
import Program.Common.DataClasses.Position;
import Program.Common.DataClasses.Worker;

import java.io.IOException;
import java.time.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Добавляет новый элемент в коллекцию.
 */
public class AddCommand implements ICommand {

    @Override
    public Boolean inputValidate(String args) {
        String[] userData = args.split(",");
        for (int i = 0; i < userData.length; i++) {
            userData[i] = userData[i].trim();
        }

        if(userData.length < 13){
            System.out.println("Not all options are specified.");
            return false;
        }else
            return true;
    }

    @Override
    public LinkedList<Worker> handle(String args,LinkedList<Worker> WorkerData) {

        Collections.sort(WorkerData);
        String[] userData = args.split(",");
        for (int i = 0; i < userData.length; i++) {
            userData[i] = userData[i].trim();
        }

        if(userData.length < 13){
            System.out.println("Not all options are specified.");
            return WorkerData;
        }

        Worker worker;
        Integer id;
        String name;
        Coordinates coordinates;
        Float salary;
        LocalDate startDate;
        LocalDateTime endDate = null;
        Position position = null;
        Person person = new Person();

        try{
            id = WorkerData.getLast().getId()+1;
        }
        catch (NoSuchElementException e){
            id = 1;
        }

        name = userData[0];
        if(name.equals("\"\""))
            System.out.println("The name field cannot be empty.");

        try {
            Float x = Float.parseFloat(userData[1]);
            Double y = Double.parseDouble(userData[2]);
            coordinates = new Coordinates(x, y);
        }
        catch (NumberFormatException e) {
            System.out.println("Datatype error for Coordinates(x/y). Fields cannot be null.");
            return WorkerData;
        }

        try {
            salary = Float.parseFloat(userData[3]);
        }
        catch (NumberFormatException e) {
            System.out.println("Salary field data type error. The field cannot be null.");
            return WorkerData;
        }

        try {
            String[] stData = userData[4].split("-");
            startDate = LocalDate.of(Integer.parseInt(stData[0]),
                    Integer.parseInt(stData[1]),
                    Integer.parseInt(stData[2]));
        }
        catch (DateTimeException | NumberFormatException e) {
            System.out.println("Error in startDate data, example: 2000-10-15. Field cannot be null");
            return WorkerData;
        }

        try {
            String s = userData[5];
            if(!s.equals("\"\"")) {
                String[] ed = userData[5].split("-");
                String[] et = userData[6].split(":");
                endDate = LocalDateTime.of(Integer.parseInt(ed[0]),
                        Integer.parseInt(ed[1]),
                        Integer.parseInt(ed[2]),
                        Integer.parseInt(et[0]),
                        Integer.parseInt(et[1]));
            }
        }
        catch (DateTimeException | NumberFormatException e) {
            System.out.println("Error in endDate data, example: 2000-10-15.");
            return WorkerData;
        }

        try {
            String pos;
            pos = userData[7];

            if (!pos.equals("\"\""))
                position = Position.valueOf(pos);
        }
        catch (IllegalArgumentException e){
            System.out.println("Incorrect position data. Example: MANAGER.");
            return WorkerData;
        }

        person = createNewPerson(userData[8],userData[9],userData[10],userData[11],userData[12], person);

        if(person != null) {
            worker = new Worker(id,
                    name,
                    coordinates,
                    ZonedDateTime.now(),
                    salary,
                    startDate,
                    endDate,
                    position,
                    person);

            WorkerData.add(worker);
        }
        return WorkerData;
    }

        @Override
        public String getName () {
            return "add";
        }

        @Override
        public String getHelp () {
            return "Adds a new element to the collection, input order:\n" +
                    "name, X, Y, salary, startDate, endDate(date, time), position, birthday(date, time), height, weight, passportID.\n" +
                    "Example: Kevin 10.5 10.5 15.5 2002-02-02 2020-12-12 15:50 MANAGER 2002-02-02 15:26 180 65 888888.\n" +
                    "To leave the field blank, enter \"\"\n." +
                    "Empty (null) can be: endDate, position, birthday и weight.";
        }

        public Person createNewPerson(String birthdayDate, String birthdayTime, String height, String weight, String passportID, Person person){

                try {
                    if (birthdayDate.equals("\"\""))
                        person.setBirthday(null);
                    else {
                        String[] perDate = birthdayDate.split("-");
                        String[] perTime = birthdayTime.split(":");

                        person.setBirthday(LocalDateTime.of(Integer.parseInt(perDate[0]),
                                Integer.parseInt(perDate[1]),
                                Integer.parseInt(perDate[2]),
                                Integer.parseInt(perTime[0]),
                                Integer.parseInt(perTime[1])));
                    }

                    } catch(DateTimeException | NumberFormatException e){
                        System.out.println("Invalid birthday field data. Example: 2000-10-12 16:35.");
                }


                try {
                    person.setHeight(Integer.parseInt(height));
                } catch (NumberFormatException e) {
                    System.out.println("Incorrect data type height.");
                }

                try {
                    if(weight.equals("\"\""))
                        person.setWeight(null);
                    else
                        person.setWeight(Float.parseFloat(weight));
                } catch (NumberFormatException e) {
                    System.out.println("Incorrect data type weight.");
                }

                try {
                    if(passportID.length()<30 && passportID.length() > 3)
                        person.setPassportID(passportID);
                    else
                        throw new IOException();
                } catch (IOException e) {
                    System.out.println("Incorrect length passportID(3<x<30).");
                }

            return person;
        }

    }

