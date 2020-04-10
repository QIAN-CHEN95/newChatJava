package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ServerWorker extends Thread {

    private Socket clientSocket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private boolean log=false;
    private String userName;

    public ServerWorker(Socket clientSocket) {
        this.clientSocket = clientSocket;
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
                log=false;
                clientSocket.close();
            }
            if (log==true){
                outputStream.write((userName+" :111 "+line+"\r\n").getBytes());
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
                    }
                }
            }
        }

    }


}