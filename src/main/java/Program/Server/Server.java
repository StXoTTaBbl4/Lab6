package Program.Server;

import java.io.IOException;

public class Server {
    public static void main(String[] args){
        ServerInit server = new ServerInit();
        server.setPath("D:\\Workers.json");
        server.execute();
    }

}
