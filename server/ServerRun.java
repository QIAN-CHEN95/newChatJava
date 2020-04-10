package server;


public class ServerRun{

    public static void main(String[] args) {
        int port = 8000;
        try {
            Server server=new Server(port);
            server.start();
            
        } catch (Exception e) {
            //TODO: handle exception
        }
    }
}