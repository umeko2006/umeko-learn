package socket.keepAlive;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Created by yingmei.qym on 2016/10/24.
 */
public class ChatServer {
    boolean started = false;
    ServerSocket ss = null;
    int clientNumer = 0;
    List<Client> clients = new ArrayList<Client>();

    public static void main(String[] args) {
        new ChatServer().start();
    }

    public void start() {
        //初始化状态，创建serverSocket
        try {
            ss = new ServerSocket(8888);
            started = true;
            System.out.println("端口已开启,占用8888端口号....");
        } catch (BindException e) {
            System.out.println("端口使用中....");
            System.out.println("请关掉相关程序并重新运行服务器！");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //无限循环监听当前8888端口是否有连接进来，发现一个连接，就创建一个socket，并基于这个socket创建一个后台用户线程
        // （此处为Client对象），用于维护跟客户端之间的通信，并将这个后台用户对象放进主线程的后台用户线程列表中。
        //一个后台用户其实就是一个包含了socket的线程，只有客户端断掉的时候这个线程才会结束。但是这个对象其实还是没有释放，
        //直到访问这个线程对象的send方法发现异常时，才会将这个对象从线程对象的list中清除掉，此时才能进行垃圾回收。
        try {
            while (started) {
                Socket s = ss.accept();
                clientNumer ++;
                Client c = new Client(s, clientNumer);
                System.out.println("a client connected!");
                new Thread(c).start();
                clients.add(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Client implements Runnable {
        private Socket s;
        private DataInputStream dis = null;
        private DataOutputStream dos = null;
        private int clientNo;
        private boolean bConnected = false;

        public Client(Socket s, int no) {
            //一个后台线程对象持有一个socket连接。并且基于这个socket连接创建了一个输入流，一个输出流。
            this.s = s;
            try {
                dis = new DataInputStream(s.getInputStream());
                dos = new DataOutputStream(s.getOutputStream());
                clientNo = no;
                bConnected = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void send(String str) {
            try {
                dos.writeUTF(str);
            } catch (IOException e) {
                clients.remove(this);
                System.out.println("对方退出了！我从List里面去掉了！");
            }
        }

        public void run() {
            try {
                while (bConnected) {
                    //每一个后台用户线程开启以后，除非是异常退出了，否则会一直进行这个循环，
                    // 从socket中读入自己对应的那个client输入的数据（这个远程客户说的话），
                    //并将这个话加上自己的名字作为前缀发送给每个用户。这样，一个人给服务端说了话，其他人都能收到消息。
                    String str = dis.readUTF();
                    System.out.println("------------来自本地服务器:" + str);
                    for (int i = 0; i < clients.size(); i++) {
                        Client c = clients.get(i);
                        c.send("no" + clientNo + ": " +str);
                    }
                }
            } catch (EOFException e) {
                System.out.println("Client closed!");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (dis != null)
                        dis.close();
                    if (dos != null)
                        dos.close();
                    if (s != null) {
                        s.close();
                    }

                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        }
    }
}
