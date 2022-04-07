package Program.Server;

import Program.Common.CollectionInit.Initializer;
import Program.Common.DataClasses.Worker;

import java.util.LinkedList;

public class Server {
    public static void main(String[] args) {
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
    }
}
