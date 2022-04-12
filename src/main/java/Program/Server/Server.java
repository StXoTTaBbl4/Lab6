package Program.Server;

/**
 * Класс для запуска сервера и его консоли {@link ServerInit#initialize()}, {@link ServerInit#consoleMonitor()}.
 */
public class Server {
    public static void main(String[] args){
        ServerInit server;
        try {
            server = new ServerInit(Integer.parseInt(args[0]),args[1]);
            server.setPath(args[2]);
            //server = new ServerInit(56666,"localhost");
            //server.setPath("D:\\Workers.json");
            server.execute();
        }catch (NumberFormatException e){
            System.out.println("port: Integer");
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Ex: 56666 0.0.0.0 D:\\Workers.json or 56666 localhost D:\\Workers.json");
        }

    }

}
