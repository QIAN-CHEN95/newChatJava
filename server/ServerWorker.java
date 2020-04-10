package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class ServerWorker extends Thread {

    private Socket clientSocket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private boolean log=false;
    private String userName;
    private Server server;

    public ServerWorker(Server server, Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.server=server;
    }
    public boolean getLog(){
        return log;
    }
    public String getUserName(){
        return userName;
    }

    @Override
    public void run() {
        welcome();
        try {
            clientSocket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private void welcome(){
        try {
            outputStream= clientSocket.getOutputStream();
            outputStream.write("-----------------------\r\n".getBytes());
            outputStream.write("welcome to java chat server ^.^\r\n".getBytes());
            outputStream.write("please login with your username and password\r\n".getBytes());
            outputStream.write("-----------------------\r\n".getBytes());
            outputStream.write("format- login username password \r\n".getBytes());
            outputStream.write("-----------------------\r\n".getBytes());
            outputStream.write("to disconnt type:quit or logoff \r\n".getBytes());

            handleLogin();

        } catch (Exception e) {
            //TODO: handle exception
        }
        
    }
    private void handleLogin() throws IOException {
        
        inputStream =clientSocket.getInputStream();
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
        String line;

        while ((line=br.readLine())!=null) {
            //disconnect from server

            if(line.equals("quit")||line.equals("logoff")){
                if (userName!=null) {
                    echoLogOff();
                }
                break;

            }
            if (log==true){
                String msg=userName+" : "+line+"\r\n";
                echoChat(msg);
            }
            if (log==false) {
                String[] token= line.split(" ");
                if (!(token.length==3 && token[0].equals("login"))) {
                    outputStream.write("format- login username password \r\n".getBytes());
                }else{
                    if ((token[1].equals("admin")&&token[2].equals("admin"))||(token[1].equals("bean")&&token[2].equals("bean"))) {
                        log=true;
                        userName=token[1];
                        outputStream.write(("you have logged in as: "+ userName+ "\r\n").getBytes());
                        outputStream.write("you have joined chat room \r\n".getBytes());
                        echoLogIn();
                    }
                }
            }
        }

    }
    private void echoLogIn() throws IOException {
        List<ServerWorker> workerList= server.getWorkerList();
        for (ServerWorker worker : workerList) {
            //notification to other user
            String msg=userName+" online \r\n";
            if (!worker.getUserName().equals(userName)) {
                worker.send(msg);
            }
            //already online
            if ((worker.getLog()==true)&&(!worker.getUserName().equals(userName)) ) {
                msg=worker.getUserName()+" online \r\n";
                send(msg);
            }
        }
    }
    private void echoLogOff() throws IOException {
        List<ServerWorker> workerList= server.getWorkerList();
        for (ServerWorker worker : workerList) {
            //notification to other user
            String msg=userName+" offline \r\n";
            if (!worker.getUserName().equals(userName)) {
                worker.send(msg);
            }
        }
    }


    private void send(String msg) throws IOException {
        outputStream.write(msg.getBytes());
    }

    private void echoChat(String msg) throws IOException{
        List<ServerWorker> workerList= server.getWorkerList();
        for (ServerWorker worker : workerList) {
            worker.send(msg);
        }
    }



}