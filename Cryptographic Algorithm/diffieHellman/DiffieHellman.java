package diffieHellman;

import java.math.BigInteger;

public class DiffieHellman {
    public static final BigInteger MIN =new BigInteger("234567891");
    public static final BigInteger MAX=new BigInteger("999999999999");
    private BigInteger prime;
    private int k;
    private BigInteger a,b;
    private BigInteger primitiveRoot;
    public DiffieHellman(int bits,int firstMaxPrimeOptimizer,long seed){
        k=bits;
        PrimeGenerator.initialize(firstMaxPrimeOptimizer,seed);
    }
    public void findPrime(){
        prime=PrimitiveRoot.generateSafePrime(k);
    }
    public void findPrimitiveRoot(BigInteger min,BigInteger max){
        primitiveRoot=PrimitiveRoot.primitiveRoot(this.prime,min,max);
    }
    public void findTwoPrime(){
        a=PrimeGenerator.nBitPrimeGenerator(k/2);
        b=PrimeGenerator.nBitPrimeGenerator(k/2);
    }
    public BigInteger computeSharedKeyA(){
        return PrimeGenerator.exponentialAndMod(primitiveRoot,a,prime);
    }
    public BigInteger computeSharedKeyB(){
        return PrimeGenerator.exponentialAndMod(primitiveRoot,b,prime);
    }
    public BigInteger verifyAndCreatePrivateKey(BigInteger A, BigInteger B){
        BigInteger key=PrimeGenerator.exponentialAndMod(A,b,prime);
        if(key.equals(PrimeGenerator.exponentialAndMod(B,a,prime))){
            return key;
        }
        else{
            throw new RuntimeException("Keys are different");
        }
    }
    public BigInteger createPrivateKeyUsingA(BigInteger A){
        return PrimeGenerator.exponentialAndMod(A,(b),prime);
    }
    public BigInteger createPrivateKeyUsingB(BigInteger B){
        return PrimeGenerator.exponentialAndMod(B,(a),prime);
    }

    public void setPrimeA(BigInteger a) {
        this.a = a;
    }
    public void setPrimeB(BigInteger b) {
        this.b = b;
    }
    public void setPrime(BigInteger prime) {
        this.prime = prime;
    }
    public void setPrimitiveRoot(BigInteger primitiveRoot) {
        this.primitiveRoot = primitiveRoot;
    }
    public BigInteger getPrime() {
        return prime;
    }
    public BigInteger getPrimitiveRoot() {
        return primitiveRoot;
    }
    BigInteger getPrimeA() {
        return a;
    }
    BigInteger getPrimeB() {
        return b;
    }
    public void printPrimes(){
        System.out.println("Prime used for modulus: " +prime.toString());
        System.out.println("Two small Prime a: "+a.toString()+" b: "+b.toString());
    }
}
