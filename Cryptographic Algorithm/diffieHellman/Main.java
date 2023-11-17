package diffieHellman;

import java.math.BigInteger;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        long seed=12345;
        for (int i = 128; i <=256 ; i+=64) {
            runAndPrint( DiffieHellman.MIN,DiffieHellman.MAX,new DiffieHellman(i,10000,seed));
        }
//        runWithUserInput();
    }
    public static void runAndPrint(BigInteger min, BigInteger max, DiffieHellman diffieHellman){
        long startTime=System.currentTimeMillis();
        diffieHellman.findPrime();
        long endTime=System.currentTimeMillis();
        System.out.println("Time needed for Safe Prime Generation:"+ (endTime-startTime)+" ms");

        startTime=System.nanoTime();
        diffieHellman.findPrimitiveRoot(min, max);
        endTime=System.nanoTime();
        System.out.println("Time needed for Primitive Root Finding:"+ (endTime-startTime)/1000+" microseconds");

        startTime=System.nanoTime();
        diffieHellman.findTwoPrime();
        endTime=System.nanoTime();
        System.out.println("Time needed for a new Prime:"+ (endTime-startTime)/1000000+" ms");

        startTime=System.nanoTime();
        BigInteger A=diffieHellman.computeSharedKeyA();
        BigInteger B=diffieHellman.computeSharedKeyB();
        endTime=System.nanoTime();
        System.out.println("Shared Key A:"+A.toString()+" B:"+B.toString());
        System.out.println("Time needed for Calculating A And B:"+ (endTime-startTime)/1000+" microseconds");

        startTime=System.nanoTime();
        BigInteger privateKey=diffieHellman.verifyAndCreatePrivateKey(A,B);
        endTime=System.nanoTime();
        System.out.println("Private Key:"+privateKey.toString());
        System.out.println("Time needed Creating Private Key:"+ (endTime-startTime)/1000+"microseconds");

        diffieHellman.printPrimes();
    }
    public static void runWithUserInput(){
        Scanner scanner= new Scanner(System.in);
        int k= scanner.nextInt();
        long seed = scanner.nextLong();
        String min = scanner.next();
        String max = scanner.next();
        runAndPrint(new BigInteger(min),new BigInteger(max),new DiffieHellman(k,10000,seed));
    }
}
