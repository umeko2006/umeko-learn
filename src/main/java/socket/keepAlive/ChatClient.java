package socket.keepAlive;

/**
 * Created by yingmei.qym on 2016/10/24.
 */
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

/**
 * @author Michael Huang
 *
 */
public class ChatClient extends Frame {
    Socket s = null;
    DataOutputStream dos = null;
    DataInputStream dis = null;
    private boolean bConnected = false;
    //文本输入框
    TextField tfTxt = new TextField();
    //文本显示区域
    TextArea taContent = new TextArea();

    Thread tRecv = new Thread(new RecvThread());

    public static void main(String[] args) {
        new ChatClient().launchFrame(8888);
    }

    /*
    主线程方法，创建一个对话框Frame，内部包含文本输入和显示组件。并且开启指向端口号port的本地（127.0.0.1）连接
     */
    public void launchFrame(int port) {
        setLocation(400, 300);
        this.setSize(300, 300);
        //把文本输入框放在下面
        add(tfTxt, BorderLayout.SOUTH);
        //把文本显示框放在上面
        add(taContent, BorderLayout.NORTH);
        pack();
        //给对话框的框架添加一个关闭窗口的监听器
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent arg0) {
                //在关闭窗口的时候，关闭流和socket
                disconnect();
                //结束当前jvm
                System.exit(0);
            }

        });
        //给文本输入框增加一个监听器，一旦输入文字，就做该做的事情：把键入的文字通过socket传输出去
        tfTxt.addActionListener(new TFListener());
        setVisible(true);
        //连接这个端口，准备工作。连接成功就可以开启
        connect(port);
        //开启接听线程。
        tRecv.start();
        //至此，存在两个线程，一个线程监听text输入（系统组件实现）做socket输出，
        // 一个监听来自于服务端的socket反馈，并做显示更新
    }

    public void connect(int port) {
        try {
            s = new Socket("127.0.0.1", port);
            dos = new DataOutputStream(s.getOutputStream());
            dis = new DataInputStream(s.getInputStream());
            System.out.println("~~~~~~~~连接成功~~~~~~~~!");
            bConnected = true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void disconnect() {
        try {
            dos.close();
            dis.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private class TFListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String str = tfTxt.getText().trim();
            tfTxt.setText("");

            try {
                dos.writeUTF(str);
                dos.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

    }

    private class RecvThread implements Runnable {

        public void run() {
            try {
                while (bConnected) {
                    String str = dis.readUTF();
                    //显示更新，获取当前的文字，附加上获取到的新的文字，做显示。
                    taContent.setText(taContent.getText() + str + '\n');
                }
            } catch (SocketException e) {
                System.out.println("退出了，bye!");
            } catch (EOFException e) {
                System.out.println("退出了，bye!");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}


