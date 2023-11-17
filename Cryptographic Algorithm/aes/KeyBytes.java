package aes;

public enum KeyBytes {

    _128(10),_Bits196(12),_Bits256(14);
    public int value;
    KeyBytes(int n){
        value=n;
    }
}
