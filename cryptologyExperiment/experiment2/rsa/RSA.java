package experiment2.rsa;

import java.math.BigInteger;

public class RSA {
    public static byte[] encrypt(byte[] data,int n,int e){
        byte[] res = new byte[data.length];
        for(int i = 0 ; i < data.length;i++){
            byte b = data[i];
            BigInteger bd = new BigInteger(String.valueOf(b));
            bd = bd.pow(e);
            bd = bd.mod(BigInteger.valueOf(n));
            res[i] = Byte.parseByte(bd.toString());
        }
        return res;
    }
    public static char[] encrypt(char[] data,int n,int e){
        char[] res = new char[data.length];
        for(int i = 0 ; i < data.length;i++){
            int b = data[i];
            BigInteger bd = new BigInteger(String.valueOf(b));
            bd = bd.pow(e);
            bd = bd.mod(BigInteger.valueOf(n));
            int a = Integer.valueOf(bd.toString());
            res[i] = (char) a;
        }
        return res;
    }

    public static char[] decrypt(char[] data,int n,int d){
        char[] res = new char[data.length];
        for(int i = 0 ; i < data.length;i++){
            int b = data[i];
            if(b < 0)
                b += 256;
            BigInteger bd = new BigInteger(String.valueOf(b));
            bd = bd.pow(d);
            bd = bd.mod(BigInteger.valueOf(n));
            int a = Integer.valueOf(bd.toString());
            res[i] = (char) a;
        }
        return res;
    }

    public static byte[] decrypt(byte[] data,int n,int d){
        byte[] res = new byte[data.length];
        for(int i = 0 ; i < data.length;i++){
            byte b = data[i];
            if(b < 0)
                b += 256;
            BigInteger bd = new BigInteger(String.valueOf(b));
            bd = bd.pow(d);
            bd = bd.mod(BigInteger.valueOf(n));
            res[i] = Byte.parseByte(bd.toString());
        }
        return res;
    }
}
