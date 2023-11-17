package socket.functionality;

import aes.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class Receiver {
    public static String receiveText(Socket socket, String key) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream=socket.getInputStream();
        while (true) {
            int read = inputStream.read();
            if (read == -1) {
                throw new RuntimeException("Socket Closed");
            }
            if (read == '\n') {
                System.out.println(stringBuilder.toString());
                    break;
            }
            stringBuilder.append((char) read);
        }
        int length = Integer.parseInt(stringBuilder.toString().split(" :")[1]);
        if(stringBuilder.toString().split(" :")[0].equals("Length and Name")){
            saveFiles(socket,key,"./FilesClient/",stringBuilder);
            return "File Received";
        }
        byte[][] encryptedText = new byte[length][];
        for (int i = 0; i < length; i++) {
            encryptedText[i] = new byte[4];
            inputStream.read(encryptedText[i]);
        }
        KeyGenerator keyGenerator = new KeyGenerator(KeyBytes._128);
        keyGenerator.initialize(key);
        Decryption decryption = new Decryption(keyGenerator,new Encryption(" ",keyGenerator));
        System.out.println("Cipher Text:"+AES.byteArrToAscii(encryptedText,false));
        byte[][] decryptedText = decryption.decryptAll(encryptedText);
        return AES.byteArrToAscii(decryptedText,true);
    }
    public static void saveFiles(Socket socket, String key,String path,StringBuilder stringBuilder) throws IOException {
        InputStream inputStream=socket.getInputStream();
        int length = Integer.parseInt(stringBuilder.toString().split(" :")[1]);
        int actual = Integer.parseInt(stringBuilder.toString().split(" :")[2]);
        String name = stringBuilder.toString().split(" :")[3];
        byte[][] encryptedText = new byte[length][];
        for (int i = 0; i < length; i++) {
            encryptedText[i] = new byte[4];
            inputStream.read(encryptedText[i]);
        }
        KeyGenerator keyGenerator = new KeyGenerator(KeyBytes._128);
        keyGenerator.initialize(key);
        Decryption decryption = new Decryption(keyGenerator,new Encryption(" ",keyGenerator));
        byte[][] decryptedText = decryption.decryptAll(encryptedText);
        byte[] bytes = new byte[actual];
        for (int i = 0; i < decryptedText.length; i++) {
            for (int j = 0; j < decryptedText[i].length && actual>0; j++) {
                bytes[i*4+j]=decryptedText[i][j];
                actual--;
            }
        }
        Path filePath = Path.of(path+ name);

        try {
            Files.write(filePath, bytes);
            System.out.println("Byte array successfully written to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
