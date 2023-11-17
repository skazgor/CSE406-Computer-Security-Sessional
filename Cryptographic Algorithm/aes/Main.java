package aes;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scanner=new Scanner(System.in);
        System.out.println("Plain Text:");
        System.out.print("In ASCII:");
        String text=scanner.nextLine();
        System.out.println("In Hex:"+AES.stringToHex(text));
        System.out.println();

        System.out.println("Key:");
        System.out.print("In ASCII:");
        String key=scanner.nextLine();
        KeyGenerator keyGenerator=new KeyGenerator(KeyBytes._128);
        keyGenerator.initialize(key);
        System.out.println("In Hex:"+AES.ByteArrToHex(keyGenerator.getBytes()));
        System.out.println();

        long startTime=System.nanoTime();
        byte[][] allRoundKey=keyGenerator.getAllRoundKey();
        long endTime=System.nanoTime();
        long keyGenerationTime=endTime-startTime;


        Encryption encryption=new Encryption(text,keyGenerator);
        startTime=System.nanoTime();
        byte[][] cypherText=encryption.encryptAll();
        endTime=System.nanoTime();
        long encryptionTime=endTime-startTime;
        System.out.println("Cypher Text:");
        System.out.println("In Hex:"+AES.ByteArrToHex(cypherText));
        System.out.println("In ASCII:"+AES.byteArrToAscii(cypherText,false));
        System.out.println();

        Decryption decryption=new Decryption(keyGenerator,encryption);
        startTime=System.nanoTime();
        byte[][] plainText=decryption.decryptAll(cypherText);
        endTime=System.nanoTime();
        long decryptionTime=endTime-startTime;
        System.out.println("Plain Text:");
        System.out.println("In Hex:"+AES.ByteArrToHex(plainText));
        System.out.println("In ASCII:"+AES.byteArrToAscii(plainText,true));
        System.out.println();

        System.out.println("Key Generation Time:"+keyGenerationTime/1000+" micro seconds");
        System.out.println("Encryption Time:"+encryptionTime/1000+" micro seconds");
        System.out.println("Decryption Time:"+decryptionTime/1000+" micro seconds");
    }
}
