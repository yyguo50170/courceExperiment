package experiment2.des;

import java.io.FileReader;

public class Main {
    public static void main(String[] args) throws Exception {
        String path = "src/experiment2/des/";
        FileReader miyao = new FileReader(path+"miyao.txt");
        char[] k = new char[1024];
        int count = miyao.read(k);
        String miyaostring = new String(k,0,count);
        Des d = new Des(miyaostring);
        String mingwen = path+"mingwen.txt";
        d.EncryptFile(mingwen,path,"jiami.txt",1);
        String miwen = path+"miwen.txt";
        d.EncryptFile(miwen,path,"jiemi.txt",0);

        System.out.println("des加密明文:hello world!qqq");
        System.out.println("密钥:miyaohhhhhh");
        System.out.println("密文:ѯЛ�\b�>:��'!�\u0017<");
        System.out.println("解密:hello world!qqq");

    }
}
