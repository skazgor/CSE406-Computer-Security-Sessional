package aes;

public class AES {
    private KeyBytes keyBytes=null;
    private KeyGenerator keyGenerator;

    public AES(String key, KeyBytes keyBytes) {
        keyGenerator=new KeyGenerator(keyBytes);
        keyGenerator.initialize(key);
        keyBytes=keyBytes;
    }
    public String encryption(String plainText){
        return null;
    }
    public String decryption(String cypherText){
        return null;
    }
    public static String stringToHex(String text){
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=0;i<text.length();i++){
            stringBuilder.append(Integer.toHexString((byte)text.charAt(i)));
        }
        return stringBuilder.toString();
    }
    public static String ByteArrToHex(byte [][] bytes){
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=0;i<bytes.length;i++){
            for(int j=0;j<bytes[i].length;j++){
                stringBuilder.append(Integer.toHexString(bytes[i][j]&255));
            }
        }
        return stringBuilder.toString();
    }
    public static String byteArrToAscii(byte [][] bytes,boolean removePadding){
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=0;i<bytes.length;i++){
            for(int j=0;j<bytes[i].length;j++){
                if(removePadding && bytes[i][j]==0){
                    break;
                }
                stringBuilder.append((char)bytes[i][j]);
            }
        }
        return stringBuilder.toString();
    }
    public static String byteArrToString(byte [] bytes){
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=0;i<bytes.length;i++){
            stringBuilder.append((char)bytes[i]);
        }
        return stringBuilder.toString();
    }
}
