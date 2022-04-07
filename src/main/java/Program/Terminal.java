package Program;

import Program.Common.CollectionInit.Initializer;
import Program.Common.Command.CommandManager;
import Program.Common.DataClasses.Worker;

import java.io.*;
import java.util.LinkedList;
/**
 *  Класс, содержащий метод main.
 */
public class Terminal {
    /** Через этот метод происходит взаимодействие пользователя с программой.
     *
     * @param args Путь к файлу с данными.
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String path = null;
        try {
            path = args[0];
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Path to file?");
            System.exit(0);
        }

        Initializer initializer = new Initializer();
        LinkedList<Worker> WorkersData = initializer.initializeCollection(path);

        if(WorkersData == null) {
            WorkersData = new LinkedList<>();
        }else{
            for (Worker worker : WorkersData) {
                initializer.DataChecker(worker);
            }
        }

        CommandManager manager = new CommandManager(path);
        while (true){
           WorkersData = manager.CommandHandler(reader.readLine(),WorkersData);
        }
    }
}
