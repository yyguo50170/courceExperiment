package experiment1.kaisa;

import java.io.*;

public class Kaisa {
    static byte[] encrypt(byte[] source,int k){
        int length = source.length;
        byte[] res = new byte[length];
        for (int i = 0 ; i < length;i++){
            byte ch = source[i];
            if(ch>= 'A' && ch <= 'Z') {
                res[i] = (byte) (((ch-'A')+k)%26+'A');
            }
            else if(ch >= 'a' && ch <= 'z') {
                res[i] = (byte) (((ch-'a')+k)%26+'a');
            }else
                res[i] = ch;
        }
        return res;
    }

    static byte[] decrypt(byte[] source,int k){
        return encrypt(source,26-k);
    }

    public static void main(String[] args) throws IOException {
        String path = "src/experiment1/kaisa/";
        FileInputStream datasource = new FileInputStream(path+"mingwen.txt");
        FileInputStream miyao = new FileInputStream(path+"miyao.txt");
        int k = Integer.parseInt(new String(miyao.readAllBytes()));
        FileOutputStream fos = new FileOutputStream(path+"miwen.txt");

        //加密
        byte[] buffer = datasource.readAllBytes();
        byte[] res = encrypt(buffer,k);
        fos.write(res);


        //解密
        byte[] jiemi = decrypt(res,k);
        System.out.println("明文:"+new String(buffer));
        System.out.println("密钥:"+k);
        System.out.println("密文:"+new String(res));
        System.out.println("解密:"+new String(jiemi));
        FileOutputStream fos1 = new FileOutputStream(path+"jiemi.txt");
        fos1.write(jiemi);
    }
}
