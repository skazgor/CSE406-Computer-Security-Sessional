package aes;

public class KeyGenerator {
    private static final int AES_MODULUS = 0x11B; // AES Modulus polynomial: x^8 + x^4 + x^3 + x + 1
    private  final int [] S_BOX = {
            0x63, 0x7C, 0x77, 0x7B, 0xF2, 0x6B, 0x6F, 0xC5, 0x30, 0x01, 0x67, 0x2B, 0xFE, 0xD7, 0xAB, 0x76,
            0xCA, 0x82, 0xC9, 0x7D, 0xFA, 0x59, 0x47, 0xF0, 0xAD, 0xD4, 0xA2, 0xAF, 0x9C, 0xA4, 0x72, 0xC0,
            0xB7, 0xFD, 0x93, 0x26, 0x36, 0x3F, 0xF7, 0xCC, 0x34, 0xA5, 0xE5, 0xF1, 0x71, 0xD8, 0x31, 0x15,
            0x04, 0xC7, 0x23, 0xC3, 0x18, 0x96, 0x05, 0x9A, 0x07, 0x12, 0x80, 0xE2, 0xEB, 0x27, 0xB2, 0x75,
            0x09, 0x83, 0x2C, 0x1A, 0x1B, 0x6E, 0x5A, 0xA0, 0x52, 0x3B, 0xD6, 0xB3, 0x29, 0xE3, 0x2F, 0x84,
            0x53, 0xD1, 0x00, 0xED, 0x20, 0xFC, 0xB1, 0x5B, 0x6A, 0xCB, 0xBE, 0x39, 0x4A, 0x4C, 0x58, 0xCF,
            0xD0, 0xEF, 0xAA, 0xFB, 0x43, 0x4D, 0x33, 0x85, 0x45, 0xF9, 0x02, 0x7F, 0x50, 0x3C, 0x9F, 0xA8,
            0x51, 0xA3, 0x40, 0x8F, 0x92, 0x9D, 0x38, 0xF5, 0xBC, 0xB6, 0xDA, 0x21, 0x10, 0xFF, 0xF3, 0xD2,
            0xCD, 0x0C, 0x13, 0xEC, 0x5F, 0x97, 0x44, 0x17, 0xC4, 0xA7, 0x7E, 0x3D, 0x64, 0x5D, 0x19, 0x73,
            0x60, 0x81, 0x4F, 0xDC, 0x22, 0x2A, 0x90, 0x88, 0x46, 0xEE, 0xB8, 0x14, 0xDE, 0x5E, 0x0B, 0xDB,
            0xE0, 0x32, 0x3A, 0x0A, 0x49, 0x06, 0x24, 0x5C, 0xC2, 0xD3, 0xAC, 0x62, 0x91, 0x95, 0xE4, 0x79,
            0xE7, 0xC8, 0x37, 0x6D, 0x8D, 0xD5, 0x4E, 0xA9, 0x6C, 0x56, 0xF4, 0xEA, 0x65, 0x7A, 0xAE, 0x08,
            0xBA, 0x78, 0x25, 0x2E, 0x1C, 0xA6, 0xB4, 0xC6, 0xE8, 0xDD, 0x74, 0x1F, 0x4B, 0xBD, 0x8B, 0x8A,
            0x70, 0x3E, 0xB5, 0x66, 0x48, 0x03, 0xF6, 0x0E, 0x61, 0x35, 0x57, 0xB9, 0x86, 0xC1, 0x1D, 0x9E,
            0xE1, 0xF8, 0x98, 0x11, 0x69, 0xD9, 0x8E, 0x94, 0x9B, 0x1E, 0x87, 0xE9, 0xCE, 0x55, 0x28, 0xDF,
            0x8C, 0xA1, 0x89, 0x0D, 0xBF, 0xE6, 0x42, 0x68, 0x41, 0x99, 0x2D, 0x0F, 0xB0, 0x54, 0xBB, 0x16,
        };
    private byte [][] bytes;
    private KeyBytes keyBytes=null;

    public KeyGenerator(KeyBytes keyBytes) {
        this.keyBytes = keyBytes;
    }

    public void initialize(String key) {
        if(keyBytes==KeyBytes._128){
            bytes=new byte[4][];
            for (int i = 0; i < 4; i++) {
                bytes[i]=new byte[4];
            }
            int i;
            for ( i= 0; i < (key.length() <= 16 ? key.length():16); i++) {
                bytes[i/4][i%4]=key.getBytes()[i];
            }
            for(;i<16;i++){
                bytes[i/4][i%4]=48;
            }
        }
        else if(keyBytes==KeyBytes._Bits196){
            bytes=new byte[6][];
            for (int i = 0; i < 6; i++) {
                bytes[i]=new byte[4];
            }
            int i;
            for ( i= 0; i < (key.length() <= 24 ? key.length():24); i++) {
                bytes[i/4][i%4]=key.getBytes()[i];
            }
            for(;i<24;i++){
                bytes[i/4][i%4]=48;
            }
        }
        else if(keyBytes==KeyBytes._Bits256){
            bytes=new byte[8][];
            for (int i = 0; i < 8; i++) {
                bytes[i]=new byte[4];
            }
            int i;
            for ( i= 0; i < (key.length() <= 32 ? key.length():32); i++) {
                bytes[i/4][i%4]=key.getBytes()[i];
            }
            for(;i<32;i++){
                bytes[i/4][i%4]=48;
            }
        }
    }
    byte [] gFunc(byte [] w,byte roundConst){
        byte[] newArr=new byte[w.length];
        for (int i = 0; i < w.length-1; i++) {
            newArr[i]=w[i+1];
        }
        newArr[w.length-1]=w[0];
        for (int i = 0; i < w.length; i++) {
            newArr[i]=(byte)S_BOX[(short)(newArr[i]&255)];
        }
        newArr[0]= (byte) (newArr[0]^roundConst);
        return newArr;
    }
    public static byte gfMultiplyModular(byte a, byte b) {
        int product = 0;
        short modulus = (short) AES_MODULUS&255;

        for (int i = 0; i < 8; i++) {
            if ((b & 1) != 0) {
                product ^= a;
            }

            boolean highBitSet = (a & 0x80) != 0;
            a <<= 1;

            if (highBitSet) {
                a ^= modulus;
            }

            b >>= 1;
        }

        return (byte) product;
    }
    public byte[][] generateAllRoundKey(byte[][] w,KeyBytes keyBytes){
        if(keyBytes==KeyBytes._128){
            byte [][] bytes=null;
            if(keyBytes==KeyBytes._128){
                bytes=new byte[44][];
                byte roundConst=1;
                for (int i = 0; i < 4; i++) {
                    bytes[i]=w[i];
                }
                for (int i = 4; i <44 ; i++) {
                    if(i%4==0){
                        bytes[i]=copyAndXor(gFunc(bytes[i-1],roundConst),bytes[i-4]);
                        roundConst=gfMultiplyModular(roundConst,(byte) 0x2);
                    }
                    else{
                        bytes[i]=copyAndXor(bytes[i-1],bytes[i-4]);
                    }
                }
            }
            return bytes;
        }
        else if( keyBytes==KeyBytes._Bits196){
            byte [][] bytes=null;
            if(keyBytes==KeyBytes._Bits196){
                bytes=new byte[52][];
                byte roundConst=1;
                for (int i = 0; i < 6; i++) {
                    bytes[i]=w[i];
                }
                for (int i = 6; i <52 ; i++) {
                    if(i%6==0){
                        bytes[i]=copyAndXor(gFunc(bytes[i-1],roundConst),bytes[i-6]);
                        roundConst=gfMultiplyModular(roundConst,(byte) 0x2);
                    }
                    else{
                        bytes[i]=copyAndXor(bytes[i-1],bytes[i-6]);
                    }
                }
            }
            return bytes;
        }
        else if(keyBytes==KeyBytes._Bits256){
            byte [][] bytes=null;
            if(keyBytes==KeyBytes._Bits256){
                bytes=new byte[60][];
                byte roundConst=1;
                for (int i = 0; i < 8; i++) {
                    bytes[i]=w[i];
                }
                for (int i = 8; i <60 ; i++) {
                    if(i%8==0){
                        bytes[i]=copyAndXor(gFunc(bytes[i-1],roundConst),bytes[i-8]);
                        roundConst=gfMultiplyModular(roundConst,(byte) 0x2);
                    }
                    else if((i%8) == 4){
                        for (int j = 0; j < 4; j++) {
                            bytes[i][j]=(byte)S_BOX[(short)(bytes[i-1][j]&255)];
                        }
                        bytes[i]=copyAndXor(bytes[i],bytes[i-8]);
                    } else if (i % 8 < 4) {
                        bytes[i]=copyAndXor(bytes[i-1],bytes[i-8]);
                    } else {
                        bytes[i]=copyAndXor(bytes[i-1],bytes[i-8]);
                    }
                }
            }
            return bytes;
        }
        return null;
    }
    private byte[] copyAndXor(byte[] b1,byte[] b2){
        byte [] newArr=new byte[b1.length];
        for (int i = 0; i < b1.length; i++) {
            newArr[i]=(byte)(b1[i]^b2[i]);
        }
        return newArr;
    }
    public byte[][] getAllRoundKey(){
        return generateAllRoundKey(this.bytes,this.keyBytes);
    }

    public int[] getS_BOX() {
        return S_BOX;
    }

    public byte[][] getBytes() {
        return bytes;
    }

    public KeyBytes getKeyBytes() {
        return keyBytes;
    }
}