package socket.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by yingmei.qym on 2016/10/24.
 */
public class HttpClient implements Runnable{
    public void run() {
       sendRequest("localhost", 8079);
    }

    public static void sendRequest(String host, int port){
        try {
            String path = "/";
            if (host == null){
                host = "www.oschina.net";
                port = 80;
            }

            Socket socket = new Socket();
            InetSocketAddress address=new InetSocketAddress(host, port);
            socket.connect(address,3000);
            OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream(),"utf-8");
            osw.write("GET " + path + " HTTP/1.1\r\n");
            osw.write("Host: " + host + " \r\n");
            //http协议必须在报文头后面再加一个换行，通知服务器发送完成，不然服务器会一直等待
            osw.write("\r\n");
            osw.flush();
            socket.shutdownOutput();

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), "utf-8"));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
            osw.close();
            bufferedReader.close();
            socket.close();
        }catch (ConnectException e) {
            System.out.println("连接失败");
        }catch (SocketTimeoutException e) {
            System.out.println("连接超时");
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Thread threadSend=new Thread(new HttpClient());
        threadSend.start();
    }
}

