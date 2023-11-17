package socket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 5091;
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket=new ServerSocket(PORT);

        while(serverSocket.isBound() && !serverSocket.isClosed()){
            Socket socket=serverSocket.accept();
            WorkerThread thread=new WorkerThread(socket);
            thread.start();
        }
    }
}