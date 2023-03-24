package mx.org.neucast.openai.test;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

public class TestSimpleClient2 {
    public static void main(String[] args) throws MalformedURLException, IOException {

        HttpsURLConnection connection = null;

        String requestURL = "https://api.openai.com/v1/completions";
        String apiKey = "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";
        String charset = "UTF-8";
        File uploadFile1 = new File("D:/Doc1.pdf");
        String boundary = Long.toHexString(System.currentTimeMillis());
        String CRLF = "\r\n"; // Line separator used in multipart/form-data.
        String jsonContent = "{\"model\":\"text-davinci-001\",\"prompt\":\"What time is it?\",\"temperature\": 1,\"max_tokens\":100}";

        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        URL url = new URL(requestURL);
        connection = (HttpsURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + apiKey);
        connection.setRequestProperty("Accept", "application/json; esl-api-version=11.0");
        connection.setHostnameVerifier(hostnameVerifier);
        OutputStream output = connection.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);

        try {

            // Add pdf file.
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + uploadFile1.getName() + "\"")
                    .append(CRLF);
            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(uploadFile1.getName()))
                    .append(CRLF);
            writer.append("Content-Transfer-Encoding: application/pdf").append(CRLF);
            writer.append(CRLF).flush();
            Files.copy(uploadFile1.toPath(), output);
            output.flush();
            writer.append(CRLF).flush();

            // add json payload
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"payload\"").append(CRLF);
            writer.append("Content-Type: application/json; charset=" + charset).append(CRLF);
            writer.append(CRLF).append(jsonContent).append(CRLF).flush();

            // End of multipart/form-data.
            writer.append("--" + boundary + "--").append(CRLF).flush();
        } catch (IOException ex) {
            System.err.println(ex);
        }

        // get and write out response code
        int responseCode = ((HttpURLConnection) connection).getResponseCode();
        System.out.println(responseCode);

        if (responseCode == 200) {

            // get and write out response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());

        } else {

            // get and write out response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());

        }
    }
}
