package experiment3;

import experiment2.des.Des;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import experiment2.md5.Md5;
import experiment2.rsa.RSA;

public class Main {
    public static void main(String[] args) throws Exception {
        String sendPath = "src/experiment3/send/";
        String receivePath = "src/experiment3/receive/";
        String miyaoPath = "src/experiment3/";

        //发送方处理
        // 获取DES使用的对称密钥
        String duichengmiyao = new String(util.Utils.getFileChar(miyaoPath+"miyao.txt"));
        Des d = new Des(duichengmiyao);
        // 使用DES算法对称密钥加密明文文件，加密后在send/miwen.txt。满足了保密要求
        d.EncryptFile(sendPath+"mingwen.txt",sendPath,"miwen.txt",1);

        // 使用RSA算法b的公钥加密对称密钥，加密后在send/jiamimiyao.txt。满足保密要求
        BufferedReader miyaoReader = new BufferedReader(new InputStreamReader(new FileInputStream(miyaoPath+"miyaob.txt")));
        int nb = Integer.valueOf(miyaoReader.readLine());
        int eb = Integer.valueOf(miyaoReader.readLine());
        int db = Integer.valueOf(miyaoReader.readLine());
        char[] jiamiduichengmiyao =  RSA.encrypt(duichengmiyao.toCharArray(),nb,eb);
        util.Utils.charArrayToFile(jiamiduichengmiyao,sendPath,"jiamimiyao.txt");

        // 使用md5算法生成对明文文件的摘要，并使用RSA算法a的私钥加密摘要。满足数据完整性要求、认证要求、和不可否认性要求
        byte[] mingwen = util.Utils.getFileByte(sendPath+"mingwen.txt");
        String md5 = Md5.md5(mingwen);
        miyaoReader = new BufferedReader(new InputStreamReader(new FileInputStream(miyaoPath+"miyaoa.txt")));
        int na = Integer.valueOf(miyaoReader.readLine());
        int ea = Integer.valueOf(miyaoReader.readLine());
        int da = Integer.valueOf(miyaoReader.readLine());
        char[] jiamimd5 = RSA.encrypt(md5.toCharArray(),na,da);
        util.Utils.charArrayToFile(md5.toCharArray(),sendPath,"md5.txt");
        util.Utils.charArrayToFile(jiamimd5,sendPath,"jiamimd5.txt");

        // 复制文件到receive文件夹下来模拟发送
        util.Utils.copyFile(sendPath+"miwen.txt",receivePath+"miwen.txt");
        util.Utils.copyFile(sendPath+"jiamimiyao.txt",receivePath+"jiamimiyao.txt");
        util.Utils.copyFile(sendPath+"jiamimd5.txt",receivePath+"jiamimd5.txt");

        //收到文件后的处理
        //使用RSA算法b的私钥解密收到的对称密钥
        jiamiduichengmiyao = util.Utils.getFileChar(receivePath+"jiamimiyao.txt");
        char[] miyao = RSA.decrypt(jiamiduichengmiyao,nb,db);
        System.out.println("解密得到的密钥:"+new String(miyao));

        //使用DES算法和解密后的密钥对密文进行解密
        Des dd = new Des(new String(miyao));
        dd.EncryptFile(receivePath+"miwen.txt",receivePath,"jiemi.txt",0);

        //使用md5算法计算明文的md5
        mingwen = util.Utils.getFileByte(receivePath+"jiemi.txt");
        String newMd5 = Md5.md5(mingwen);
        System.out.println("计算得到的md5："+newMd5);

        //使用RSA算法a的公钥解密MD5文件
        char[] receiveMd5 = RSA.decrypt(util.Utils.getFileChar(receivePath+"jiamimd5.txt"),na,ea);
        //和根据解密后的文件的md5做比较
        String receiveMd5String = new String(receiveMd5);
        System.out.println("解密得到的md5："+receiveMd5String);
        System.out.println("md5是否相等："+receiveMd5String.equals(newMd5));
    }
}
