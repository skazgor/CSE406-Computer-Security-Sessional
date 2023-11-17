package socket.functionality;

import aes.Encryption;
import aes.KeyBytes;
import aes.KeyGenerator;

import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Sender {
    public static void sendText(String text, Socket socket, String key) {
        KeyGenerator keyGenerator = new KeyGenerator(KeyBytes._128);
        keyGenerator.initialize(key);
        Encryption encryption = new Encryption(text, keyGenerator);
        byte [][] encryptedText = encryption.encryptAll();
        try {
            socket.getOutputStream().write(("Length :"+encryptedText.length+"\n").getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < encryptedText.length; i++) {
            try {
                socket.getOutputStream().write(encryptedText[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void sendFiles(String name, Socket socket, String key) {
        KeyGenerator keyGenerator = new KeyGenerator(KeyBytes._128);
        keyGenerator.initialize(key);
        Path filePath = Path.of("./FilesServer/"+name);
        byte[] bytes=null;
        try {
            bytes= Files.readAllBytes(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Encryption encryption = new Encryption(bytes, keyGenerator);
        byte [][] encryptedText = encryption.encryptAllBytes();
        try {
            socket.getOutputStream().write(("Length and Name :"+encryptedText.length+" :"+bytes.length+" :"+name+"\n").getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < encryptedText.length; i++) {
            try {
                socket.getOutputStream().write(encryptedText[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
