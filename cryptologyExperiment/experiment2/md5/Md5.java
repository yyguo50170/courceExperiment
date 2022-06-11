package experiment2.md5;

import util.Utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Miracle Luna on 2019/11/18
 */
public class Md5 {
    /**
     * 将数据进行 MD5 加密，并以16进制字符串格式输出
     * @param data
     * @return
     */
//    public static String md5(String data) {
//        try {
//            byte[] md5 = md5(data.getBytes("utf-8"));
//            return toHexString(md5);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }

    /**
     * 将字节数组进行 MD5 加密
     * @param data
     * @return
     */
//    public static byte[] md5(byte[] data) {
//        try {
//            MessageDigest md = MessageDigest.getInstance("md5");
//            return md.digest(data);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return new byte[]{};
//    }

    public static String md5(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(data);
        String hashedPwd = new BigInteger(1, md.digest()).toString(16);
        return hashedPwd;
    }

    /**
     * 将加密后的字节数组，转换成16进制的字符串
     * @param md5
     * @return
     */
    private static String toHexString(byte[] md5) {
        StringBuilder sb = new StringBuilder();
        System.out.println("md5.length: " + md5.length);
        for (byte b : md5) {
            sb.append(Integer.toHexString(b & 0xff));
        }
        return sb.toString();
    }

//    public static void main(String[] args) {
//        String password = "password";
//        String md5HexStr = md5(password);
//        System.out.println("==> MD5 加密前: " + password);
//        System.out.println("==> MD5 加密后: " + md5HexStr);
//    }
}