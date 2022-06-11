package experiment2.rsa;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Arrays;

import util.Utils;

import static util.Utils.*;


public class Main {
    public static void main(String[] args) throws Exception {
        //int n = 299,e = 151,d = 181;
        String path = "src/experiment2/rsa/";
        char[] mingwen = getFileChar(path+"mingwen.txt");
        BufferedReader miyaoReader = new BufferedReader(new InputStreamReader(new FileInputStream(path+"miyao.txt")));
        int n = Integer.valueOf(miyaoReader.readLine());
        int e = Integer.valueOf(miyaoReader.readLine());
        int d = Integer.valueOf(miyaoReader.readLine());
        //int n = 33,e = 7,d = 3;
        //int n= 77,e = 13,d = 37;
        //int n = 143,e = 7,d = 103;
        char[] miwen = RSA.encrypt(mingwen,n,e);
        charArrayToFile(miwen,path,"miwen.txt");
        char[] jiemi = RSA.decrypt(miwen,n,d);
        charArrayToFile(jiemi,path,"jiemi.txt");

        System.out.println("rsa加密明文:"+new String(mingwen));
        System.out.println("密钥: n="+n+" e="+e+" d="+d);
        System.out.println("密文:"+new String(miwen));
        System.out.println("解密:"+new String(jiemi));

    }
}