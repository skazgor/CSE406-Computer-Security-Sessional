package diffieHellman;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PrimeGenerator {
    private static long seed=0;
    private static Random rand;
    private static List<Integer> firstFewPrimeList=null;
    public static void initialize(int firstMaxPrimeOptimizer,long seed){
        if(seed==0){
            rand=new Random();
        }
        else {
            PrimeGenerator.seed=seed;
            rand=new Random(seed);
        }
        primeListInitializer(firstMaxPrimeOptimizer);
    }
    private static void primeListInitializer(int n){
        if(firstFewPrimeList==null){
            firstFewPrimeList=firstFewPrimeGenerator(n);
        }
    }
    public static BigInteger nBitRandomOddNumber(int k,Random rand){
        BigInteger temp;
        do{
           temp=new BigInteger(k,rand);
           temp.setBit(k);
        }
        while(!temp.testBit(0));
        return temp;
    }
    public static  boolean isPrimeCandidate(BigInteger num){
        PrimeGenerator.primeListInitializer(1000000);
        for (int n:
             firstFewPrimeList) {
            if(num.remainder(new BigInteger(Integer.toString(n))).equals(BigInteger.ZERO)){
                return false;
            }
        }
        return true;
    }
    public static boolean rabinMillerPrimalityCheck(BigInteger bigInteger,int iteration){
        if(bigInteger.compareTo(BigInteger.ONE)<=0){
            return false;
        }
        if(bigInteger.compareTo(BigInteger.valueOf(3))<=0){
            return true;
        }
        if(bigInteger.mod(BigInteger.TWO).equals(BigInteger.ZERO)){
            return false;
        }
        BigInteger evenComponent=bigInteger.subtract(BigInteger.ONE);
        int maxDivisionByTwo=0;
        while(evenComponent.mod(BigInteger.TWO).equals(BigInteger.ZERO)){
            evenComponent=evenComponent.divide(BigInteger.TWO);
            maxDivisionByTwo++;
        }
        Random rand=new Random();
        for (int i = 0; i <iteration ; i++) {
            BigInteger roundTester;
            roundTester=PrimeGenerator.nBitRandomOddNumber(bigInteger.bitLength(),rand);
            if(trialComposite(roundTester,evenComponent,bigInteger,maxDivisionByTwo)){
                return false;
            }
        }
        return true;
    }
    private static List<Integer> firstFewPrimeGenerator(int n){
        List<Integer> primelist=new ArrayList<>((int)Math.log(n));
        boolean []isPrime=new boolean[n+1];
        for (int i=0;i<n;i++){
            isPrime[i]=true;
        }
        for (int i = 2; i <Math.sqrt(n+1) ; i++) {
            if(!isPrime[i])continue;
            for (int j = 1; j <=n/i ; j++) {
                isPrime[j*i]=false;
            }
        }
        for (int i = 3; i <=n ; i++) {
            if (isPrime[i]) primelist.add(i);
        }
        return primelist;
    }
    public static BigInteger exponentialAndMod(BigInteger base, BigInteger exponent,BigInteger modulus){
        BigInteger val=BigInteger.ONE;
        for(int i=exponent.bitLength()-1;i>=0;i--){
            if (exponent.testBit(i)){
                val=val.pow(2).multiply(base).mod(modulus);
            }
            else{
                val=val.pow(2).mod(modulus);
            }
        }
        return val;
    }
    private static boolean trialComposite(BigInteger roundTester,BigInteger evenComponent,BigInteger millerRabinCandidate,int maxDivisionByTwo){
        BigInteger result=exponentialAndMod(roundTester,evenComponent,millerRabinCandidate);
        if(result.equals(BigInteger.ONE)){
            return false;
        }
        for (int i = 0; i <maxDivisionByTwo ; i++) {
            result=exponentialAndMod(roundTester,evenComponent.multiply(new BigInteger(Integer.toString((int)Math.pow(2,i)))),millerRabinCandidate);
            if(result.equals(millerRabinCandidate.subtract(BigInteger.ONE))){
                return false;
            }
        }
        return true;
    }
    public static BigInteger nBitPrimeGenerator(int k){
        BigInteger primeCandidate;
        do{
            do{
                primeCandidate=PrimeGenerator.nBitRandomOddNumber(k,rand);
            }
            while(!PrimeGenerator.isPrimeCandidate(primeCandidate));
        }
        while(!PrimeGenerator.rabinMillerPrimalityCheck(primeCandidate,30));
        return primeCandidate;
        
    }
}
