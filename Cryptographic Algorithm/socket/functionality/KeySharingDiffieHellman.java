package socket.functionality;

import aes.AES;
import diffieHellman.DiffieHellman;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Socket;

public class KeySharingDiffieHellman {
    public static String Initializer(Socket socket, int bits, long seed){
        DiffieHellman diffieHellman=new DiffieHellman(bits,100000,seed);
        diffieHellman.findPrime();
        System.out.println("Prime :"+diffieHellman.getPrime().toString());
        diffieHellman.findPrimitiveRoot(DiffieHellman.MIN,DiffieHellman.MAX);
        System.out.println("Primitive Root :"+diffieHellman.getPrimitiveRoot().toString());
        diffieHellman.findTwoPrime();
        BigInteger keyA=diffieHellman.computeSharedKeyA();
        System.out.println("Key A :"+keyA.toString());

        try {
            OutputStream outputStream= socket.getOutputStream();
            InputStream inputStream=socket.getInputStream();
            outputStream.write(diffieHellman.getPrime().toString().getBytes());
            outputStream.write("\n".getBytes());
            outputStream.write(diffieHellman.getPrimitiveRoot().toString().getBytes());
            outputStream.write("\n".getBytes());
            outputStream.write(keyA.toString().getBytes());
            outputStream.write("\n".getBytes());

            BigInteger keyB=readNextBigInteger(inputStream);
            BigInteger privateKey=diffieHellman.createPrivateKeyUsingB(keyB);
            System.out.println(privateKey.toString());
            return AES.byteArrToString(privateKey.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static String responder(Socket socket, int bits, long seed) throws IOException {
        DiffieHellman diffieHellman=new DiffieHellman(bits,100000,seed);
        BigInteger prime=readNextBigInteger(socket.getInputStream());
        BigInteger primitiveRoot=readNextBigInteger(socket.getInputStream());
        BigInteger keyA=readNextBigInteger(socket.getInputStream());

        diffieHellman.setPrime(prime);
        diffieHellman.setPrimitiveRoot(primitiveRoot);
        diffieHellman.findTwoPrime();

        BigInteger keyB=diffieHellman.computeSharedKeyB();

        OutputStream outputStream= socket.getOutputStream();
        outputStream.write(keyB.toString().getBytes());
        outputStream.write("\n".getBytes());

        BigInteger privateKey=diffieHellman.createPrivateKeyUsingA(keyA);
        System.out.println(privateKey.toString());
        return AES.byteArrToString(privateKey.toByteArray());
    }
    private static BigInteger readNextBigInteger(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder=new StringBuilder();
        while (true){
            int i=inputStream.read();
            if(i==-1){
                break;
            }
            if(i=='\n'){
                System.out.println("Read: "+stringBuilder.toString());
                break;
            }
            stringBuilder.append((char)i);
        }
        return new BigInteger(stringBuilder.toString());
    }
}
