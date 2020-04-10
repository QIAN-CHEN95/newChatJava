package server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

	private int port;

    public Server(int port) {
        this.port=port;
    }
    

    @Override
    public void run() {
        newServer(port);


    }
    private void handleCS(ServerSocket serverSocket){
        try {
            System.out.println("waiting for connection ...");
            Socket clientSocket=serverSocket.accept();
            System.out.println("connected to "+clientSocket);
            ServerWorker worker=new ServerWorker(clientSocket);
            worker.start();
        } catch (Exception e) {
            //TODO: handle exception
        }

    }

    private void newServer(int port){
        try {
            ServerSocket serverSocket=new ServerSocket(port);
            while (true) {
                handleCS(serverSocket);
            }

        } catch (Exception e) {
            //TODO: handle exception
        }
    }

}