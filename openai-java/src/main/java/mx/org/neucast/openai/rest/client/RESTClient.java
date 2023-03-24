package mx.org.neucast.openai.rest.client;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.StatusLine;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * @author B11160
 */
public class RESTClient {
    public RESTClient() {
        super();
    }

    public String get(String uri, String apiKey) {
        String jsonResponseString = "";

        try (final CloseableHttpClient httpclient = HttpClients.custom()
                .setConnectionManager(configureBasicHttpClientConnectionManager())
                .build()) {

            final HttpGet httpGet = new HttpGet(uri);

            httpGet.addHeader(HttpHeaders.CONNECTION, "keep-alive");
            httpGet.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            httpGet.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey);

            System.out.println("Executing request " + httpGet.getMethod() + " " + httpGet.getUri());

            jsonResponseString = httpclient.execute(httpGet, response -> {
                System.out.println("----------------------------------------");
                System.out.println(httpGet + " -> " + new StatusLine(response));

                String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");

                EntityUtils.consume(response.getEntity());

                return responseString;
            });

            // System.out.println(jsonResponseString);

        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        } catch (URISyntaxException urise) {
            throw new RuntimeException(urise);
        }
        return jsonResponseString;
    }

    public String post(String uri, String apiKey, String jsonQuery) {
        String jsonResponseString = "";

        try (final CloseableHttpClient httpclient = HttpClients.custom()
                .setConnectionManager(configureBasicHttpClientConnectionManager())
                .build()) {

            final HttpPost httpPost = new HttpPost(uri);

            httpPost.addHeader(HttpHeaders.CONNECTION, "keep-alive");
            httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            httpPost.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey);

            System.out.println("Query: " + jsonQuery);

            final StringEntity entity = new StringEntity(jsonQuery);
            httpPost.setEntity(entity);

            System.out.println("Executing request " + httpPost.getMethod() + " " + httpPost.getUri());

            jsonResponseString = httpclient.execute(httpPost, response -> {
                System.out.println("----------------------------------------");
                System.out.println(httpPost + " -> " + new StatusLine(response));

                String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");

                EntityUtils.consume(response.getEntity());

                return responseString;
            });

            // System.out.println(jsonResponseString);

        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        } catch (URISyntaxException urise) {
            throw new RuntimeException(urise);
        }
        return jsonResponseString;
    }

    private static BasicHttpClientConnectionManager configureBasicHttpClientConnectionManager() {

        final TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;

        final SSLContext sslContext;
        try {
            sslContext = SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        }

        final SSLConnectionSocketFactory sslsf =
                new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        final Registry<ConnectionSocketFactory> socketFactoryRegistry =
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("https", sslsf)
                        .register("http", new PlainConnectionSocketFactory())
                        .build();

        final BasicHttpClientConnectionManager connectionManager =
                new BasicHttpClientConnectionManager(socketFactoryRegistry);

        return connectionManager;
    }
}
