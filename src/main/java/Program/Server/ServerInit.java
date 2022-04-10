package Program.Server;

import Program.Common.CollectionInit.Initializer;
import Program.Common.Command.CommandManager;
import Program.Common.Command.Commands.SaveCommand;
import Program.Common.Communicator;
import Program.Common.DataClasses.Transporter;
import Program.Common.DataClasses.Worker;
import Program.Common.Serializer;

import java.io.*;
import java.net.*;
import java.nio.file.Watchable;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerInit {
    private final CommandManager manager = new CommandManager();
    private LinkedList<Worker> WorkersData;
    private String path;

    private LinkedList<Worker> getWorkersData() {
        return WorkersData;
    }

    private String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setWorkersData(LinkedList<Worker> data) {
        this.WorkersData = data;
    }

    void initialize() {
        try {
            path = getPath();
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            System.out.println("Path to file?");
            e.printStackTrace();
            System.exit(0);
        }

        Initializer initializer = new Initializer();
        WorkersData = initializer.initializeCollection(path);

        if (WorkersData == null) {
            WorkersData = new LinkedList<>();
        } else {
            for (Worker worker : WorkersData) {
                initializer.DataChecker(worker);
            }
        }
        setWorkersData(WorkersData);

        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(56666);
        }catch (SocketException e){
            System.out.println("Failed to create socket.");
        }

        byte[] buffer = new byte[65536];
        Serializer serializer = new Serializer();
        Transporter transporter = new Transporter();
        DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);

        //System.out.println("Ожидаем данные...");

        while(true)
        {

            try {
                //Получаем данные
                socket.receive(incoming);
                byte[] data = incoming.getData();
                transporter = (Transporter) serializer.deserialize(data);

                System.out.println("Сервер получил: " + transporter.getCommand());

                WorkersData = manager.CommandHandler(transporter.getCommand(), WorkersData);
                setWorkersData(WorkersData);

                //Отправляем данные клиенту
                data = serializer.serialize(transporter);
                DatagramPacket dp = new DatagramPacket(data, data.length, incoming.getAddress(), incoming.getPort());
                socket.send(dp);
            }catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }

        /*
        byte[] buffer = new byte[65536];
        DatagramPacket income = new DatagramPacket(buffer, buffer.length);
        Transporter transporter = new Transporter();
        Serializer serializer = new Serializer();
        Communicator communicator = new Communicator();


        while (true){
            try {
                transporter = communicator.receive(socket, serializer);
                System.out.println(transporter.getCommand());

            }catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
                System.out.println("Failed to receive data");
            }

            //обработка

            transporter.setMessage("Принято");

            if(manager.validate(transporter))
                try {
                    communicator.send(transporter, serializer, socket, income.getAddress(), income.getPort());
                }catch (IOException e){
                    System.out.println("Response sending error.");
                }

        }

         */

        //=================================================================

        /*
        //Создание сокета
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(56666);
            System.out.println(serverSocket.getLocalSocketAddress());
        }
        catch (IOException e){
            System.out.println("Cannot create socket.");
            System.exit(0);
        }
        System.out.println("Server started!");

        Socket socket;
        BufferedWriter writer = null;
        BufferedReader reader = null;
        try {
            socket = serverSocket.accept();
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("I/O error during socket creation.");
            System.exit(0);
        }

        while (true) {
            try {
                String request = reader.readLine();
                System.out.println(request);

                writer.write("There is your answer.");
                writer.newLine();
                writer.flush();
            }catch (IOException e){
                System.out.println("Ошибка I/O в процессе обработки запроса.");
            }
            catch (NullPointerException e){
                try {
                    writer.write("There is no request.");
                    writer.newLine();
                    writer.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        }
        */
    }

    public void consoleMonitor() {
        System.out.println("Console opened.");
        BufferedReader reader =new BufferedReader(new InputStreamReader(System.in));
        String s;
        while(true) {
            try {
                s = reader.readLine();
                if (s.contains("save")){
                    try{
                        String[] p = s.split(" ");
                        SaveCommand saveCommand = new SaveCommand();
                        saveCommand.handle(p[1],getWorkersData());
                    }
                    catch (ArrayIndexOutOfBoundsException | NullPointerException e){
                        System.out.println("You must specify the path to save the collection.");
                    }
                }
                else if(s.equals("shutdown")){
                    System.exit(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void execute(){
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.submit(this::consoleMonitor);
        executorService.submit(this::initialize);

        executorService.shutdown();
    }


}
