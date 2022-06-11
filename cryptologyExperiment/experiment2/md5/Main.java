package experiment2.md5;

import util.Utils;
import java.math.BigInteger;
import java.security.MessageDigest;

public class Main {
    public static void main(String[] args) throws Exception {
        String path = "src/experiment2/md5/";
        String mingwen = path+"/mingwen.txt";
        byte[] mingwenbyte = Utils.getFileByte(mingwen);
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(mingwenbyte);
        String hashedPwd = new BigInteger(1, md.digest()).toString(16);
        Utils.byteArrayToFile(hashedPwd.getBytes(),path,"md5.txt");
        System.out.println("md5加密明文:"+new String(mingwenbyte));
        System.out.println("生成的MD5摘要:"+hashedPwd);
    }
}
