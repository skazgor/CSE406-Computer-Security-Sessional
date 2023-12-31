package aes;

public class Decryption {
    private final int[] []InverseMixer={
            {0xe,0xb,0xd,0x9},
            {0x9,0xe,0xb,0xd},
            {0xd,0x9,0xe,0xb},
            {0xb,0xd,0x9,0xe}
    };
    private final int [] InverseSBox={
            0x52, 0x09, 0x6A, 0xD5, 0x30, 0x36, 0xA5, 0x38, 0xBF, 0x40, 0xA3, 0x9E, 0x81, 0xF3, 0xD7, 0xFB,
            0x7C, 0xE3, 0x39, 0x82, 0x9B, 0x2F, 0xFF, 0x87, 0x34, 0x8E, 0x43, 0x44, 0xC4, 0xDE, 0xE9, 0xCB,
            0x54, 0x7B, 0x94, 0x32, 0xA6, 0xC2, 0x23, 0x3D, 0xEE, 0x4C, 0x95, 0x0B, 0x42, 0xFA, 0xC3, 0x4E,
            0x08, 0x2E, 0xA1, 0x66, 0x28, 0xD9, 0x24, 0xB2, 0x76, 0x5B, 0xA2, 0x49, 0x6D, 0x8B, 0xD1, 0x25,
            0x72, 0xF8, 0xF6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xD4, 0xA4, 0x5C, 0xCC, 0x5D, 0x65, 0xB6, 0x92,
            0x6C, 0x70, 0x48, 0x50, 0xFD, 0xED, 0xB9, 0xDA, 0x5E, 0x15, 0x46, 0x57, 0xA7, 0x8D, 0x9D, 0x84,
            0x90, 0xD8, 0xAB, 0x00, 0x8C, 0xBC, 0xD3, 0x0A, 0xF7, 0xE4, 0x58, 0x05, 0xB8, 0xB3, 0x45, 0x06,
            0xD0, 0x2C, 0x1E, 0x8F, 0xCA, 0x3F, 0x0F, 0x02, 0xC1, 0xAF, 0xBD, 0x03, 0x01, 0x13, 0x8A, 0x6B,
            0x3A, 0x91, 0x11, 0x41, 0x4F, 0x67, 0xDC, 0xEA, 0x97, 0xF2, 0xCF, 0xCE, 0xF0, 0xB4, 0xE6, 0x73,
            0x96, 0xAC, 0x74, 0x22, 0xE7, 0xAD, 0x35, 0x85, 0xE2, 0xF9, 0x37, 0xE8, 0x1C, 0x75, 0xDF, 0x6E,
            0x47, 0xF1, 0x1A, 0x71, 0x1D, 0x29, 0xC5, 0x89, 0x6F, 0xB7, 0x62, 0x0E, 0xAA, 0x18, 0xBE, 0x1B,
            0xFC, 0x56, 0x3E, 0x4B, 0xC6, 0xD2, 0x79, 0x20, 0x9A, 0xDB, 0xC0, 0xFE, 0x78, 0xCD, 0x5A, 0xF4,
            0x1F, 0xDD, 0xA8, 0x33, 0x88, 0x07, 0xC7, 0x31, 0xB1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xEC, 0x5F,
            0x60, 0x51, 0x7F, 0xA9, 0x19, 0xB5, 0x4A, 0x0D, 0x2D, 0xE5, 0x7A, 0x9F, 0x93, 0xC9, 0x9C, 0xEF,
            0xA0, 0xE0, 0x3B, 0x4D, 0xAE, 0x2A, 0xF5, 0xB0, 0xC8, 0xEB, 0xBB, 0x3C, 0x83, 0x53, 0x99, 0x61,
            0x17, 0x2B, 0x04, 0x7E, 0xBA, 0x77, 0xD6, 0x26, 0xE1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0C, 0x7D,
    };
    KeyGenerator keyGenerator;
    byte[][] key;
    private Encryption encryption;

    public Decryption(KeyGenerator keyGenerator,Encryption encryption) {
        this.keyGenerator = keyGenerator;
        key=keyGenerator.getAllRoundKey();
        this.encryption=encryption;
    }
    byte[][] substituteByte(byte[][] input){
        byte[][] newArr=new byte[input.length][];
        for (int i = 0; i < input.length; i++) {
            newArr[i]=new byte[input[i].length];
            for (int j = 0; j < input[i].length; j++) {
                newArr[i][j]=(byte)InverseSBox[(short)(input[i][j]&255)];
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
                newArr[(j+i)%4][i]=input[j][i];
            }
        }
        return newArr;
    }
    public byte[][] addRoundKey(byte[][] input,int round){
        return encryption.addRoundKey(input, round);
    }
    byte[][] mixColumns(byte [][] inputs){
        byte[][] newArr=new byte[inputs.length][];
        for (int i = 0; i < inputs.length; i++) {
            newArr[i]=new byte[inputs[i].length];
            for (int j = 0; j < inputs[i].length; j++) {
                newArr[i][j]=0;
                for (int k = 0; k < inputs[i].length; k++) {
                    newArr[i][j]^=KeyGenerator.gfMultiplyModular((byte) InverseMixer[i][k],inputs[j][k]);
                }
            }
        }
        return encryption.transpose(newArr);
    }
    byte[][] decrypt(byte [][] newArr){
        newArr=addRoundKey(newArr,keyGenerator.getKeyBytes().value);
        newArr=shiftArray(newArr);
        newArr=substituteByte(newArr);
        for (int i = keyGenerator.getKeyBytes().value-1; i >0; i--) {
            newArr=addRoundKey(newArr,i);
            newArr=mixColumns(newArr);
            newArr=shiftArray(newArr);
            newArr=substituteByte(newArr);
        }
        newArr=addRoundKey(newArr,0);
        return newArr;
    }
    public byte[][] decryptAll(byte[][] input){
        byte[][] newArr=new byte[input.length][];
        for (int i = 0; i < input.length/4; i++) {
           byte[][] temp=new byte[4][4];
            for (int j = 0; j < 4; j++) {
                temp[j]=input[i*4+j];
            }
            byte [][] decrypted=decrypt(temp);
            for (int j = 0; j < 4; j++) {
                newArr[i*4+j]=decrypted[j];
            }
        }
        return newArr;
    }
}
