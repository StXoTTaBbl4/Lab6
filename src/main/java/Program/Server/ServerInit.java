package Program.Server;

import Program.Common.CollectionInit.Initializer;
import Program.Common.Command.CommandManager;
import Program.Common.Command.Commands.SaveCommand;
import Program.Common.DataClasses.Transporter;
import Program.Common.DataClasses.Worker;
import Program.Common.Serializer;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerInit {
    private LinkedList<Worker> WorkersData;
    private String path;
    private int port;
    private String ip;

    public ServerInit(int port, String ip) {
        this.port = port;
        this.ip = ip;
    }

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

        final CommandManager manager = new CommandManager(path);

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
        InnerServerTransporter innerTransporter = new InnerServerTransporter();
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

                innerTransporter.setWorkersData(WorkersData);
                innerTransporter.setArgs(transporter.getCommand());
                innerTransporter.setIncome(incoming);
                innerTransporter.setSocket(socket);
                innerTransporter = manager.CommandHandler(innerTransporter);
                setWorkersData(innerTransporter.getWorkersData());

                transporter.setMessage(innerTransporter.getMsg());
                //Отправляем данные клиенту
                data = serializer.serialize(transporter);
                DatagramPacket dp = new DatagramPacket(data, data.length, incoming.getAddress(), incoming.getPort());
                socket.send(dp);
            }catch (SocketException e){
                transporter.setMessage("A program execution error occurred, message was not generated.");
                byte[] data = incoming.getData();
                try {
                    data = serializer.serialize(transporter);
                    DatagramPacket dp = new DatagramPacket(data, data.length, incoming.getAddress(), incoming.getPort());
                    socket.send(dp);
                } catch (IOException ex) {ex.printStackTrace();}
            }
            catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }

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
                        InnerServerTransporter transporter = new InnerServerTransporter();
                        transporter.setArgs(p[1]);
                        transporter.setWorkersData(getWorkersData());
                        SaveCommand saveCommand = new SaveCommand();
                        saveCommand.handle(transporter);
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
