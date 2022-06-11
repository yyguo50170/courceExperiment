package util;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

public class Utils {
    public static void byteArrayToFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "\\" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据路径获取文件的byte数组
     *
     * @param path
     * @return
     */
    public static byte[] getFileByte(String path) {
        FileChannel fc = null;
        byte[] result = null;
        try {
            fc = new RandomAccessFile(path, "r").getChannel();
            MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0,
                    fc.size()).load();
            result = new byte[(int) fc.size()];
            if (byteBuffer.remaining() > 0) {
                byteBuffer.get(result, 0, byteBuffer.remaining());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void charArrayToFile(char[] chars, String filePath, String fileName) throws IOException {
        FileWriter writer = new FileWriter(filePath + fileName);
        writer.write(chars, 0, chars.length);
        writer.close();
    }

    public static char[] getFileChar(String path) throws Exception {
        FileReader fr = new FileReader(path);
        char[] buffer = new char[1024];
        int count = 0;
        StringBuilder sb = new StringBuilder();
        while ((count = fr.read(buffer)) != -1) {
            sb.append(buffer, 0, count);
        }
        return sb.toString().toCharArray();
    }

    public static char[] byteArrayToCharArray(byte[] bytes, int length) {
        char[] res = new char[length];
        for (int i = 0; i < length; i++) {
            res[i] = (char) bytes[i];
        }
        return res;
    }

    public static void copyFile(String srcPath, String descPath) {
        try {
            int bytesum = 0;
            int byteread;
            File oldfile = new File(srcPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(srcPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(descPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
    }
}