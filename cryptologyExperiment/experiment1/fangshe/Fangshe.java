package experiment1.fangshe;

import java.io.*;

public class Fangshe {
    static char[] encrypt(char[] text,int length,int k1,int k2) {
        char[] result = new char[length];
        int i,z=0;
        for(i=0;i<length;i++) {
            //判断大小写
            if (text[i] >= 'A' && text[i] <= 'Z'){
                result[z]= (char) ((k1*(text[i]-'A')+k2)%26+'A');
            } else if (text[i] >= 'a' && text[i] <= 'z'){
                result[z]= (char) ((k1*(text[i]-'a')+k2)%26+'a');
            } else{  //判断是不是空格
                result[z] = text[i];
            }
            z++;
        }
        return result;
    }

    //解密
    static char[] decrypt(char[]text,int length,int k1,int k2) {
        char[] result = new char[length];
        int i,z=0;
        int k3 = k1;
        switch (k1){
            case 3:
                k3 = 9;
                break;
            case 5:
                k3 =21;
                break;
            case 7:
                k3 = 15;
                break;
            case 9:
                k3 = 3;
                break;
            case 11:
                k3 = 19;
                break;
            case 15:
                k3 = 7;
                break;
            case 17:
                k3 = 23;
                break;
            case 19:
                k3 = 11;
                break;
            case 21:
                k3 = 5;
                break;
            case 23:
                k3 = 17;
                break;
            case 25:
                k3 = 25;
                break;
        }
        for(i=0;i<length;i++) {
            //判断大小写
            if (text[i] >= 'A' && text[i] <= 'Z'){
                result[z]= (char) ((k3*((text[i]-'A')-k2))%26+'A');
                if(k3*((text[i]-'A')-k2) < 0){
                    result[z] = (char) (result[z] + 26);
                }
            } else if (text[i] >= 'a' && text[i] <= 'z'){
                result[z]= (char) ((k3*((text[i]-'a')-k2))%26+'a');
                if(k3*((text[i]-'a')-k2) < 0){  //处理负数
                    result[z] = (char) (result[z] + 26);
                }
            } else{  //判断是不是空格
                result[z] = text[i];
            }
            z++;
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        String path = "src/experiment1/fangshe/";
        FileReader datasource = new FileReader(path+"mingwen.txt");
        char[] mingwen = new char[1024];
        int length = datasource.read(mingwen);
        BufferedReader miyaoReader = new BufferedReader(new InputStreamReader(new FileInputStream(path+"miyao.txt")));
        int k1 = Integer.valueOf(miyaoReader.readLine());
        int k2 = Integer.valueOf(miyaoReader.readLine());

        char[] miwen = encrypt(mingwen,length,k1,k2);
        FileWriter miwenWriter = new FileWriter(path+"miwen.txt");
        miwenWriter.write(miwen);
        miwenWriter.close();

        char[] jiemi = decrypt(miwen,length,k1,k2);
        FileWriter jiemiWriter = new FileWriter(path+"jiemi.txt");
        jiemiWriter.write(jiemi,0,length);
        jiemiWriter.close();
        System.out.println("仿射加密明文:"+new String(mingwen,0,length));
        System.out.println("密钥: k1="+k1+" k2="+k2);
        System.out.println("密文:"+new String(miwen));
        System.out.println("解密:"+new String(jiemi));

    }
}
