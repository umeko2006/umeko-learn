package socket.https.multitime;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.Socket;
import java.security.KeyStore;

/**
 * SSL Client
 *
 */
public class SSLClient {

    private static final String DEFAULT_HOST                    = "127.0.0.1";
    private static final int    DEFAULT_PORT                    = 7777;

    private static final String CLIENT_KEY_STORE_PASSWORD       = "123456";
    private static final String CLIENT_TRUST_KEY_STORE_PASSWORD = "123456";

    private SSLSocket           sslSocket;

    /**
     * 启动客户端程序
     *
     * @param args
     */
    public static void main(String[] args) {
        SSLClient client = new SSLClient();
        client.init();
        client.process();
    }

    /**
     * 通过ssl socket与服务端进行连接,并且发送一个消息
     */
    public void process() {
        if (sslSocket == null) {
            System.out.println("ERROR");
            return;
        }
        OutputStream output = null;
        DataOutputStream dos = null;
        InputStream input = null;
        DataInputStream dis = null;
        try {
            Integer number = 0;
            boolean ifContinue = true;
            output = sslSocket.getOutputStream();
            dos = new DataOutputStream(output);
            input = sslSocket.getInputStream();
            dis = new DataInputStream(input);
            while (ifContinue){
                String msg = number.toString();
                Thread.sleep(3000l);
                sendMsg(dos, msg);
                receiveMsg(dis);
                //如果想要退出debug时在此处敲入  ifContinue = false
                number = number + 1;
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                sslSocket.close();
                if (dos != null){
                    dos.close();
                }
                if (output != null){
                    output.close();
                }
                if (dis != null){
                    dis.close();
                }
                if (input != null){
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private void sendMsg(DataOutputStream dos, String msg) throws IOException {
        dos.writeUTF(msg);
        dos.flush();
        System.out.println("client send:" + msg);
    }

    private String receiveMsg(DataInputStream dis) throws IOException {
        String msg = dis.readUTF();
        System.out.println("client receive:" + msg);
        return msg;
    }

    /**
     * <ul>
     * <li>ssl连接的重点:</li>
     * <li>初始化SSLSocket</li>
     * <li>导入客户端私钥KeyStore，导入客户端受信任的KeyStore(服务端的证书)</li>
     * </ul>
     */
    public void init() {
        try {
            SSLContext ctx = SSLContext.getInstance("SSL");

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");

            KeyStore ks = KeyStore.getInstance("JKS");
            KeyStore tks = KeyStore.getInstance("JKS");

            ks.load(new FileInputStream("C:\\Users\\yingmei.qym\\.keystore"), CLIENT_KEY_STORE_PASSWORD.toCharArray());
            tks.load(new FileInputStream("C:\\Users\\yingmei.qym\\.keystore"), CLIENT_TRUST_KEY_STORE_PASSWORD.toCharArray());

            kmf.init(ks, CLIENT_KEY_STORE_PASSWORD.toCharArray());
            tmf.init(tks);

            ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            sslSocket = (SSLSocket) ctx.getSocketFactory().createSocket(DEFAULT_HOST, DEFAULT_PORT);
            System.out.println("sslSocket created");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
