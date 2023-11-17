package diffieHellman;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class PrimitiveRoot {
    public static BigInteger generateSafePrime(int bits){
        while(true){
            BigInteger prime=PrimeGenerator.nBitPrimeGenerator(bits);
            BigInteger temp=prime.subtract(BigInteger.ONE);
            while(temp.mod(BigInteger.TWO).equals(BigInteger.ZERO)){
                temp=temp.divide(BigInteger.TWO);
            }
            if(PrimeGenerator.rabinMillerPrimalityCheck(temp,20)){
                return prime;
            }
            for(int i=2;i<=32;i++){
                temp=prime.multiply(BigInteger.valueOf(i)).add(BigInteger.ONE);
                if(PrimeGenerator.rabinMillerPrimalityCheck(temp,20)){
                    return temp;
                }
            }
        }
    }
    public static List<BigInteger> safePrimeFactorization(BigInteger prime){
        BigInteger temp=prime.subtract(BigInteger.ONE);
        List<BigInteger> primeFactors=new ArrayList<>(2);
        primeFactors.add(BigInteger.TWO);
        while(temp.mod(BigInteger.TWO).equals(BigInteger.ZERO)){
            temp=temp.divide(BigInteger.TWO);
        }
        primeFactors.add(temp);
        return primeFactors;
    }
    public static BigInteger primitiveRoot(BigInteger prime,BigInteger min, BigInteger max){
        for(BigInteger i=min;i.compareTo(max)<=0;i=i.add(BigInteger.ONE)) {
            boolean flag=false;
            for (BigInteger primeFactors :
                    safePrimeFactorization(prime)) {
                if (PrimeGenerator.exponentialAndMod(i,prime.subtract(BigInteger.ONE).divide(primeFactors),prime).equals(BigInteger.ONE)){
                    flag=true;
                    break;
                }
            }
            if(!flag){
                return i;
            }
        }
        throw new RuntimeException("No primitive root found");
    }
}
