package socket.http;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by yingmei.qym on 2016/10/24.
 */
public class HttpServer implements Runnable{
    public void run() {
        ServerSocket server;
        Socket socket;
        try{
            server=new ServerSocket(8079);
            System.out.println("正在等待8079端口的请求");
            while(true){
                socket=server.accept();
                if(socket!=null){
                    new Thread(new TestServerThread(socket)).start();
                }
            }
        }catch (Exception e) {
            System.out.println("异常");
        }
    }

    public static void main(String[] args) {
        Thread threadReceive=new Thread(new HttpServer());
        threadReceive.start();
    }

}
