package socket.https.multitime;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created with .
 * Date: 14-4-10
 * Time: 下午3:09
 * To change this template use File | Settings | File Templates.
 */
public class SSLHttpClient {

//    JAVA调用HTTPS 链接
    public static void main(String[] args) {
        SSLHttpClient client = new SSLHttpClient();
        client.connect();
    }

    private void connect() {

        sendPost("https://localhost/index.jsp", "name1=value1&name2=value2");

    }


    /**
     * 向指定URL发送POST方法的请求
     * @param url    发送请求的URL
     * @param params 请求参数，请求参数应该是name1=value1&name2=value2的形式。
     * @return URL所代表远程资源的响应
     */
    public static String sendPost(String url, String params) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {

            URL realUrl = new URL(url);

            SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new SecureRandom());


            HttpsURLConnection conn = (HttpsURLConnection) realUrl.openConnection();

            conn = (HttpsURLConnection) realUrl.openConnection();  // 打开和URL之间的连接
            conn.setSSLSocketFactory(context.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());


            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");


            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(params);
            // flush输出流的缓冲
            out.flush();

            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += "\n" + line;
            }

            System.out.println(result);
        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

}

class TrustAnyTrustManager implements X509TrustManager {
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[]{};
    }
}


class TrustAnyHostnameVerifier implements HostnameVerifier {
    public boolean verify(String hostname, SSLSession session) {
        // 直接Pass，全部信任
        return true;
    }
}
