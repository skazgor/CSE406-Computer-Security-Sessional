package aes;

import java.util.ArrayList;
import java.util.List;

public class Encryption {
    private static final byte [][] Mixer={
            {2,3,1,1},
            {1,2,3,1},
            {1,1,2,3},
            {3,1,1,2}
    };
    List<String> inputs=new ArrayList<>();
    List<byte[][]> byteList;
    KeyGenerator keyGenerator;
    byte[][] key;

    public Encryption(String input,KeyGenerator keyGenerator) {
        for (int i = 0; i < input.length(); i+=16) {
            inputs.add(input.substring(i,Math.min(i+16,input.length())));
        }
        this.keyGenerator=keyGenerator;
        key=keyGenerator.getAllRoundKey();
    }

    public Encryption(byte[] bytes, KeyGenerator keyGenerator) {
        byteList=new ArrayList<>();
        int i;
        for (i = 0; i < bytes.length; i+=16) {
            byte[][] temp=new byte[4][];
            for (int j = 0; j < 4; j++) {
                temp[j]=new byte[4];
            }
            for (int j = 0; j < 16; j++) {
                if(i+j<bytes.length){
                    temp[j/4][j%4]=bytes[i+j];
                }
                else{
                    temp[j/4][j%4]=0;
                }
            }
            byteList.add(temp);
        }
        this.keyGenerator=keyGenerator;
        key=keyGenerator.getAllRoundKey();
    }

    byte[][] createBytes(String s){
        byte [][] bytes=new byte[4][];
        for (int i = 0; i < 4; i++) {
            bytes[i]=new byte[4];
        }
        int i;
        for (i= 0; i < (Math.min(s.length(), 16)); i++) {
            bytes[i/4][i%4]=s.getBytes()[i];
        }
        for(;i<16;i++){
            bytes[i/4][i%4]=0;
        }
        return bytes;
    }
    byte[][] substituteByte(byte[][] input){
        byte[][] newArr=new byte[input.length][];
        for (int i = 0; i < input.length; i++) {
            newArr[i]=new byte[input[i].length];
            for (int j = 0; j < input[i].length; j++) {
                newArr[i][j]=(byte)keyGenerator.getS_BOX()[(short)(input[i][j]&255)];
            }
        }
        return newArr;
    }
    byte[][] shiftArray(byte [][] input){
        byte[][] newArr=new byte[input.length][];
        for (int i = 0; i < input.length; i++) {
            newArr[i]=new byte[input[i].length];
        }
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
                newArr[j][i]=input[(j+i)%4][i];
            }
        }
        return newArr;
    }
    byte[][] mixColumns(byte [][] inputs){
        byte[][] newArr=new byte[inputs.length][];
        for (int i = 0; i < inputs.length; i++) {
            newArr[i]=new byte[inputs[i].length];
            for (int j = 0; j < inputs[i].length; j++) {
                newArr[i][j]=0;
                for (int k = 0; k < inputs[i].length; k++) {
                    newArr[i][j]^=KeyGenerator.gfMultiplyModular(Mixer[i][k],inputs[j][k]);
                }
            }
        }
        return transpose(newArr);
    }
    public byte[][] addRoundKey(byte[][] input,int round){
        byte[][] newArr=input.clone();
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
                newArr[i][j]^=key[4*round+i][j];
            }
        }
        return newArr;
    }
    byte[][] encrypt(String input){
        byte[][] newArr=createBytes(input);
        return encrypt(newArr);
    }
    void print(byte[][] arr){
        for (byte[] bytes : arr) {
            for (byte aByte : bytes) {
                System.out.print(Integer.toHexString( (aByte & 255)) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
    void printColumn(byte[][] arr){
        for(int i=0;i< arr.length;i++){
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(Integer.toHexString((int)(arr[j][i]&255))+" ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public List<String> getInputs() {
        return inputs;
    }
    public byte[][] transpose(byte[][] arr){
        byte[][] newArr=new byte[arr.length][];
        for (int i = 0; i < arr.length; i++) {
            newArr[i]=new byte[arr[i].length];
            for (int j = 0; j < arr[i].length; j++) {
                newArr[i][j]=arr[j][i];
            }
        }
        return newArr;
    }
    public byte[][] encryptAll(){
        byte[][] newArr=new byte[inputs.size()*4][];
        for (int i = 0; i < inputs.size(); i++) {
            byte[][] temp=encrypt(inputs.get(i));
            for (int j = 0; j < temp.length; j++) {
                newArr[i*4+j]=temp[j];
            }
        }
        return newArr;
    }
    public byte[][] encrypt(byte[][] input){
        byte[][] newArr;
        newArr=addRoundKey(input,0);
        for (int i = 1; i < keyGenerator.getKeyBytes().value; i++) {
            newArr=substituteByte(newArr);
            newArr=shiftArray(newArr);
            newArr=mixColumns(newArr);
            newArr=addRoundKey(newArr,i);
        }
        newArr=substituteByte(newArr);
        newArr=shiftArray(newArr);
        newArr=addRoundKey(newArr,10);
        return newArr;
    }
    public byte[][] encryptAllBytes(){
        byte[][] newArr=new byte[byteList.size()*4][];
        for (int i = 0; i < byteList.size(); i++) {
            byte[][] temp=encrypt(byteList.get(i));
            for (int j = 0; j < temp.length; j++) {
                newArr[i*4+j]=temp[j];
            }
        }
        return newArr;
    }
}
