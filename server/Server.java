package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {

	private int port;
    private ArrayList<ServerWorker> workerList=new ArrayList<>();
    public Server(int port) {
        this.port=port;
    }
    
    public List<ServerWorker> getWorkerList(){
        return workerList;
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
            ServerWorker worker=new ServerWorker(this,clientSocket);
            workerList.add(worker);
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