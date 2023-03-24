package mx.org.neucast.openai.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Exception;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class SkipVerification implements Runnable {

    private static final String TAG = SkipVerification.class.toString();
    private String server_port;
    private String serverIP;

    public SkipVerification(String serverIP, String server_port) {
        this.serverIP = serverIP;
        this.server_port = server_port;
    }

    public void run() {
        try {
            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            URL url = new URL("https://api.openai.com/v1/completions");
            //URL url = new URL("https://" + serverIP + ":" + server_port + "/v1/completions");
            InputStream inStream = null;

            try {
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Authorization", "Bearer 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");
                urlConnection.setHostnameVerifier(hostnameVerifier);
                inStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
            } catch (Exception e) {
                System.out.println("error fetching data from server: " + e.getMessage());
            } finally {
                if (inStream != null) {
                    inStream.close();
                }
            }
        } catch (Exception e) {
            System.out.println("error initializing SkipVerificationn thread: " + e.getMessage());
        }
    }
}