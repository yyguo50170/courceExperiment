import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

public class Client extends Socket {

    private static String host = "127.0.0.1"; // 服务端IP
    private static int port = 15000; // 服务端端口
    private Socket client;
    private byte[] clientPublicKey;
    private byte[] clientPrivateKey;
    private byte[] clientKey;

    public Client() throws Exception {
        super(host, port);
        this.client = this;
        System.out.println("cliect成功连接服务端");
    }

    public void initClientKey() throws Exception {
        Map<String, Object> keyMap = DH.initKey();
        clientPublicKey = DH.getPublicKey(keyMap);
        clientPrivateKey = DH.getPrivateKey(keyMap);
    }

    /**
     * 向服务端传输文件
     *
     * @throws Exception
     */
    public void sendFile(String filepath) throws Exception {

        FileInputStream fis = null;
        DataOutputStream dos = null;
        try {
            //发送客户端公钥给server
            initClientKey();
            System.out.println("开始发送客户端公钥");
            OutputStream outputStream = client.getOutputStream();
            outputStream.write(clientPublicKey);
            outputStream.flush();

            InputStream inputStream = client.getInputStream();
            byte[] serverPublicKey = new byte[1024];
            //接收服务器端公钥
            int n = inputStream.read(serverPublicKey);
            serverPublicKey = Arrays.copyOf(serverPublicKey, n);
            clientKey = DH.getSecretKey(serverPublicKey, clientPrivateKey);
            String key = Base64.getEncoder().encodeToString(clientKey);
            System.out.println("客户端密钥===" + key);

            Des d = new Des(key);
            filepath = d.EncryptFile(filepath, 1);
            File file = new File(filepath);
            if (file.exists()) {
                fis = new FileInputStream(file);
                dos = new DataOutputStream(client.getOutputStream());

                // 文件名
                dos.writeUTF(file.getName());
                dos.flush();

                // 开始传输文件
                System.out.println("======== 开始传输文件 [File path：" + filepath + "]========");
                byte[] bytes = new byte[1024];
                int length = 0;
                while ((length = fis.read(bytes)) != -1) {
                    dos.write(bytes, 0, length);
                    dos.flush();
                }
                System.out.println("======== 文件传输成功 ========");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null)
                fis.close();
            if (dos != null)
                dos.close();
            client.close();
        }
    }

    public static void main(String[] args) {
        System.getProperties().setProperty("jdk.crypto.KeyAgreement.legacyKDF", "true");
        try {
            Client client = new Client(); // 启动客户端连接
            String filepath = "src\\files\\a.txt";
            //String filepath = "src\\files\\b.jpg";
            //String filepath = "src\\files\\c.docx";
            client.sendFile(filepath); // 传输文件
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}