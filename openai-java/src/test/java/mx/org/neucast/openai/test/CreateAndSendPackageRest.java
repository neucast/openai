package mx.org.neucast.openai.test;
import java.io.BufferedReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

import javax.net.ssl.HttpsURLConnection;

public class CreateAndSendPackageRest {

    public static void main(String[] args) throws MalformedURLException, IOException {

        HttpsURLConnection connection = null;

        String requestURL = "https://sandbox.esignlive.com/api/packages";
        String apiKey = "api_key";
        String charset = "UTF-8";
        File uploadFile1 = new File("C:/Users/hhaidary/Desktop/PDFs/doc1.pdf");
        String boundary = Long.toHexString(System.currentTimeMillis());
        String CRLF = "\r\n"; // Line separator used in multipart/form-data.
        String jsonContent = "{\"roles\":[{\"locked\":false,\"emailMessage\":{\"content\":\"\"},\"attachmentRequirements\":[],\"reassign\":false,\"specialTypes\":[],\"id\":\"Sender\",\"data\":null,\"type\":\"SIGNER\",\"index\":0,\"signers\":[{\"auth\":{\"challenges\":[],\"scheme\":\"NONE\"},\"company\":\"Silanis\",\"firstName\":\"yourFirst\",\"lastName\":\"yourLast\",\"phone\":\"\",\"email\":\"your.email@example.com\",\"knowledgeBasedAuthentication\":null,\"language\":\"en\",\"title\":\"Silanis\",\"external\":null,\"professionalIdentityFields\":[],\"userCustomFields\":[],\"delivery\":{\"email\":true,\"provider\":false,\"download\":true},\"group\":null,\"signature\":null,\"address\":null,\"data\":null,\"name\":\"\",\"specialTypes\":[]}],\"name\":\"Sender\"},{\"locked\":false,\"emailMessage\":{\"content\":\"\"},\"attachmentRequirements\":[],\"reassign\":false,\"specialTypes\":[],\"id\":\"Signer\",\"data\":null,\"type\":\"SIGNER\",\"index\":0,\"signers\":[{\"auth\":{\"challenges\":[],\"scheme\":\"NONE\"},\"company\":\"\",\"firstName\":\"signerFirst\",\"lastName\":\"signerLast\",\"phone\":\"\",\"email\":\"signer.email@example.com\",\"knowledgeBasedAuthentication\":null,\"language\":\"en\",\"title\":\"\",\"external\":null,\"professionalIdentityFields\":[],\"userCustomFields\":[],\"delivery\":{\"email\":false,\"provider\":false,\"download\":false},\"group\":null,\"id\":\"Signer\",\"signature\":null,\"address\":null,\"data\":null,\"name\":\"\",\"specialTypes\":[]}],\"name\":\"Signer\"}],\"documents\": [{\"approvals\":[{\"role\":\"Signer\",\"signed\":null,\"accepted\":null,\"data\":null,\"fields\":[{\"page\":0,\"subtype\":\"FULLNAME\",\"width\":200,\"binding\":null,\"extract\":false,\"extractAnchor\":null,\"left\":175,\"top\":165,\"validation\":null,\"height\":50,\"data\":null,\"type\":\"SIGNATURE\",\"value\":\"\"}],\"name\":\"\"},{\"role\":\"Sender\",\"signed\":null,\"accepted\":null,\"data\":null,\"fields\":[{\"page\":0,\"subtype\":\"FULLNAME\",\"width\":200,\"binding\":null,\"extract\":false,\"extractAnchor\":null,\"left\":550,\"top\":165,\"validation\":null,\"height\":50,\"data\":null,\"type\":\"SIGNATURE\",\"value\":\"\"}],\"name\":\"\"}],\"name\": \"sampleAgreement\"}],\"name\": \"Test Package REST\", \"type\":\"PACKAGE\", \"language\":\"en\", \"emailMessage\":\"\", \"description\":\"New Package\",\"autoComplete\":true,\"status\":\"SENT\"}";

        URL url = new URL(requestURL);
        connection = (HttpsURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connection.setRequestProperty("Authorization", "Basic " + apiKey);
        connection.setRequestProperty("Accept", "application/json; esl-api-version=11.0");
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