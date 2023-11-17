package socket.server;

import socket.functionality.KeySharingDiffieHellman;
import socket.functionality.Sender;

import java.net.Socket;
import java.util.Scanner;

public class WorkerThread extends Thread {
    private Socket socket;
    public WorkerThread(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        String key= KeySharingDiffieHellman.Initializer(socket, 128, 100000);
        System.out.println(key.toString());
        Scanner scanner=new Scanner(System.in);
        while (true){
            String message=scanner.nextLine();
            if (message.split(" ")[0].equals("send")){
                try {
                    Sender.sendFiles(message.split(" ")[1],socket,key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                continue;
            }
            Sender.sendText(message,socket,key);
            if(message.equals("exit")){
                break;
            }
        }
    }
}
