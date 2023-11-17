package socket.client;

import socket.functionality.KeySharingDiffieHellman;
import socket.functionality.Receiver;

import java.io.IOException;
import java.net.Socket;

public class Client {
    private static final int PORT = 5091;
    private static final String HOST = "localhost";
    public static void main(String[] args) throws  IOException {
        Socket socket=new Socket(HOST,PORT);
        String key= KeySharingDiffieHellman.responder(socket, 128, 100);
        System.out.println(key.toString());
        while (true){
            String message= Receiver.receiveText(socket,key);
            System.out.println(message);
            if (message.equals("exit")){
                break;
            }
        }
        socket.close();
    }
}
