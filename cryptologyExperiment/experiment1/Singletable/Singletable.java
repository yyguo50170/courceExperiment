package experiment1.Singletable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.FieldPosition;
import java.util.HashSet;

public class Singletable {
    static char[] table = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

    static void update(char[] key,int length) {

        HashSet set = new HashSet();
        int j = 0;
        for(int i = 0 ; i < length;i++){
            char ch = key[i];
            if(!Character.isLetter(ch))
                continue;
            ch = Character.toLowerCase(ch);
            if(set.contains(ch))
                continue;
            table[j++] = ch;
            set.add(ch);
        }
        char ch = 'a';
        while(j < 26){
            if(!set.contains(ch)){
                table[j++] = ch;
            }
            ch++;
        }
    }

    static char[] encrypt(char[] string,int length){
        char[] res = new char[length];
        for(int i = 0 ; i < length;i++){
            char ch = string[i];
            if(ch >='a' && ch <= 'z'){
                res[i] = table[ch-'a'];
            }else
                res[i] = ch;
        }
        return res;
    }

    static char[] decrypt(char[] string,int length){
        char[] res = new char[length];
        int i = 0;
        for(char ch:string){
            if(ch >='a' && ch <='z'){
                for(int j = 0 ; j < 26;j++){
                    if(ch == table[j]){
                        res[i++] = (char) (j+'a');
                        break;
                    }
                }
            }else
                res[i++] = ch;
        }
        return res;
    }

    public static void main(String[] args) throws Exception {
        String path = "src/experiment1/Singletable/";
        FileReader datasource = new FileReader(path+"mingwen.txt");
        FileReader miyao = new FileReader(path+"miyao.txt");
        char[] k = new char[1024];
        int count = miyao.read(k);
        update(k,count);
        int count1 = count;
        char[] message = new char[1024];
        count = datasource.read(message);
        char[] miwen = encrypt(message,count);
        FileWriter miwenWriter = new FileWriter(path+"miwen.txt");
        miwenWriter.write(miwen);
        miwenWriter.close();
        char[] jiemi = decrypt(miwen,miwen.length);
        FileWriter jiemiWriter = new FileWriter(path+"jiemi.txt");

        System.out.println("单表加密明文:"+new String(message,0,count));
        System.out.println("密钥:"+new String(k,0,count1));
        System.out.println("密文:"+new String(miwen));
        System.out.println("解密:"+new String(jiemi));
        jiemiWriter.write(jiemi);
        jiemiWriter.close();
    }
}
