package qi.com.demop;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {
    //单利模式  懒汉式加载
    private static RetrofitUtils retrofitUtils;
    private static ApiService apiService;
    String CER_CLIENT ="-----BEGIN CERTIFICATE-----\n" +
            "MIIDXTCCAkUCCQDNhr7+xMtU3jANBgkqhkiG9w0BAQUFADBoMQswCQYDVQQGEwJD\n" +
            "TjELMAkGA1UECAwCeDExCzAJBgNVBAcMAngyMQswCQYDVQQKDAJ4MzELMAkGA1UE\n" +
            "CwwCeDUxCzAJBgNVBAMMAmJ3MRgwFgYJKoZIhvcNAQkBFgkxQDE2My5jb20wHhcN\n" +
            "MTgwOTE3MTEyNjI2WhcNMjgwOTE0MTEyNjI2WjB5MQswCQYDVQQGEwJDTjELMAkG\n" +
            "A1UECAwCeDExCzAJBgNVBAcMAngyMQ4wDAYDVQQKDAViYXdlaTEPMA0GA1UECwwG\n" +
            "YmF3ZWkyMQswCQYDVQQDDAJidzEiMCAGCSqGSIb3DQEJARYTMTg2MDAxNTE1NjhA\n" +
            "MTYzLmNvbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAMRn5BnuG5Qm\n" +
            "GBJV+aBdVpCPkXNs7sCZPpKT6K6gi1t2L88DOiZqsIi+06KsN55w/51YeuAHYq9w\n" +
            "QFx9X34eFB/n//SKA/qkWznxZdtsAJUD3hkKkR3jhj+JP1EZWxwgIP5Dp1RyuBxH\n" +
            "gEoe7UmK9o/V2hJ3HTAYF20vQquFltucl5svnmtQvF4aofhFQ3gqXYvXD6pxcIuI\n" +
            "UOePK49hnlxz7v5t5s/0VXHHz+5THsEg14oW+kAPFKVPS59tjQV7LzDMXjunEBzc\n" +
            "A/Jslafx32BF4Fy1aCbWCmIJSKou9MBnrP1MuheIpMO1qEMBXx/9MuLMFdnyj20N\n" +
            "9M+WlaMBiMUCAwEAATANBgkqhkiG9w0BAQUFAAOCAQEAJf/W2zTuf9D36js7766T\n" +
            "xpfWCVy0POqkdXNKvPThd/U6Qwi2QXc0CmNvr02lfVRu11cX4inR9RiJUXWoeG7J\n" +
            "DDWBSBPKTpeF8+k2w+DjDAkE3mj3iCQdeydkhCUYquSxtFNC6mFZ9zrkMs7sGuBc\n" +
            "GoDnueL8B2IiNfLtA3vUzvAkqh9b7rOBk1VXem4JFnIoisFufdzH1RhNWxZTgtlG\n" +
            "+Po5VSrMpKgtPYLHFIprMIUwGfW7j36hhvnEArEVXLWjY3hhNvyJ4jBf0WRp44GA\n" +
            "8OZ1zDEyVxxtOAQXXlfiYusPuy5Wup2P7RYo17xMVoHeQg6yF+iszlBHoJ5250iv\n" +
            "kA==\n" +
            "-----END CERTIFICATE-----\n";
    /**
     * 实现了 X509TrustManager
     * 通过此类中的 checkServerTrusted 方法来确认服务器证书是否正确
     * */
    static class MyTrustManager implements X509TrustManager {
        X509Certificate cert;
        MyTrustManager(X509Certificate cert) {
            this.cert = cert;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // 我们在客户端只做服务器端证书校验。
            throw new UnsupportedOperationException();
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // 确认服务器端证书和代码中 hard code 的 CRT 证书相同。
            if (chain[0].equals(this.cert)) {
                Log.i("Jin", "checkServerTrusted Certificate from server is valid!");
                return;// found match
            }
            throw new CertificateException("checkServerTrusted No trusted server cert found!");
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
    /**
     * 进行 HTTPS 访问测试
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static RetrofitUtils getInstance(){
        if (retrofitUtils == null){
            synchronized (RetrofitUtils.class){
                if (retrofitUtils == null){
                    retrofitUtils = new RetrofitUtils();
                }
            }
        }
        return retrofitUtils;
    }
    public ApiService getMyServer() throws KeyManagementException, NoSuchAlgorithmException {
        return apiService==null?configRetrofit(ApiService.class):apiService;
    }
    //配置
    private <T> T configRetrofit(Class<T> service) throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("TLS");
        //信任证书管理,这个是由我们自己生成的,信任我们自己的服务器证书
        TrustManager tm = new MyTrustManager(readCert(CER_CLIENT));
        sc.init(null,  new TrustManager[] { tm }, new SecureRandom());
        OkHttpClient client = new OkHttpClient().newBuilder()
               // .addInterceptor(new LoggingInterceptor())
                .sslSocketFactory(sc.getSocketFactory(), (X509TrustManager) tm)
                .hostnameVerifier(hostnameVerifier)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        return retrofit.create(service);
    }
    //主机地址验证
    static final HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
    /**
     * 根据字符串读取出证书
     * @param cer
     * @return
     */
    private static X509Certificate readCert(String cer) {
        if (cer == null || cer.trim().isEmpty())
            return null;
        InputStream caInput = new ByteArrayInputStream(cer.getBytes());
        X509Certificate cert = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509","BC");
            cert = (X509Certificate) cf.generateCertificate(caInput);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (caInput != null) {
                    caInput.close();
                }
            } catch (Throwable ex) {
            }
        }
        return cert; }
}
