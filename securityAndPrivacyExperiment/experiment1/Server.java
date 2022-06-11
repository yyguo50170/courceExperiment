import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

public class Server extends ServerSocket {

    private static final int SERVER_PORT = 15000; // 服务端端口
    private byte[] serverPublicKey;
    private byte[] serverPrivateKey;
    private byte[] serverKey;

    public Server() throws Exception {
        super(SERVER_PORT);
    }

    public void initServerKey() throws Exception {
        Map<String, Object> keyMap = DH.initKey();
        serverPublicKey = DH.getPublicKey(keyMap);
        serverPrivateKey = DH.getPrivateKey(keyMap);
    }

    public void receiveFile(Socket socket) {
        DataInputStream dis = null;
        FileOutputStream fos = null;

        try {
            //接收客户端公钥
            InputStream inputStream = socket.getInputStream();
            System.out.println("开始接收客户端公钥");
            byte[] clientPublicKey = new byte[1024];
            int n = inputStream.read(clientPublicKey);
            Arrays.copyOf(clientPublicKey, n);

            //初始化服务器密钥
            initServerKey();
            //发送服务器公钥
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(serverPublicKey);
            outputStream.flush();
            serverKey = DH.getSecretKey(clientPublicKey, serverPrivateKey);
            String key = Base64.getEncoder().encodeToString(serverKey);
            System.out.println("服务器密钥===" + key);

            dis = new DataInputStream(socket.getInputStream());
            // 文件名
            String fileName = dis.readUTF();
            String path = "src\\receive\\" + fileName;
            File file = new File(path);
            fos = new FileOutputStream(file);

            // 开始接收文件
            byte[] bytes = new byte[1024];
            int length = 0;
            while ((length = dis.read(bytes)) != -1) {
                fos.write(bytes, 0, length);
                fos.flush();
            }
            System.out.println("======== 文件接收成功 [File Name：" + fileName + "] ");
            //解密文件
            Des d = new Des(key);
            path = d.EncryptFile(path, 0);
            System.out.println("======== 文件解密成功 [File path：" + path);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
                if (dis != null)
                    dis.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        System.getProperties().setProperty("jdk.crypto.KeyAgreement.legacyKDF", "true");
        try {
            Server server = new Server(); // 启动服务端
            while (true) {
                Socket socket = server.accept();
                server.receiveFile(socket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}