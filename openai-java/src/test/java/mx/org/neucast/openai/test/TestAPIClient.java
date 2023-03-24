package mx.org.neucast.openai.test;

import org.apache.hc.client5.http.classic.methods.HttpGet;
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
import org.apache.hc.core5.http.message.StatusLine;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author B11160
 */
public class TestAPIClient {
    // https://www.baeldung.com/httpclient-ssl
    public static void main(final String[] args) throws Exception {

//        GET https://api.openai.com/v1/models HTTP/1.1
//        Accept-Encoding: gzip,deflate
//        Host: api.openai.com
//        Connection: Keep-Alive
//        User-Agent: Apache-HttpClient/4.1.1 (java 1.5)
//        Authorization: Basic OnNrLWQ1ZTNDMUJXeVM0cG13ZmxXWFFQVDNCbGJrRkpTOGs2WlJib2ttb1JuOFBqRGhZWA==

//        API-KEY:
//        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


        final HttpGet getMethod = new HttpGet("https://api.openai.com");

        final TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;

        final SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        final SSLConnectionSocketFactory sslsf =
                new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        final Registry<ConnectionSocketFactory> socketFactoryRegistry =
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("https", sslsf)
                        .register("http", new PlainConnectionSocketFactory())
                        .build();

        final BasicHttpClientConnectionManager connectionManager =
                new BasicHttpClientConnectionManager(socketFactoryRegistry);

//        try( CloseableHttpClient httpClient = HttpClients.custom()
//                .setConnectionManager(connectionManager)
//                .build();
//
//             CloseableHttpResponse response = (CloseableHttpResponse) httpClient
//                     .execute(getMethod, new CustomHttpClientResponseHandler())) {
//
//            final int statusCode = response.getCode();
//            assertThat(statusCode, equalTo(HttpStatus.SC_OK));
//        }


        try (final CloseableHttpClient httpclient = HttpClients.custom()
                .setConnectionManager(connectionManager)
//                .setDefaultCredentialsProvider(CredentialsProviderBuilder.create()
//                        .add(new HttpHost("api.openai.com", 80), "", "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".toCharArray())
//                        .build())
                .build()) {

            final HttpGet httpGet = new HttpGet("https://api.openai.com/v1/models");

            httpGet.addHeader(HttpHeaders.CONNECTION, "keep-alive");
            httpGet.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            httpGet.addHeader(HttpHeaders.AUTHORIZATION, "Bearer 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");
//            httpGet.addHeader(HttpHeaders.CACHE_CONTROL, "max-age=0");
//            httpGet.addHeader(HttpHeaders.AUTHORIZATION, "Basic OnNrLWQ1ZTNDMUJXeVM0cG13ZmxXWFFQVDNCbGJrRkpTOGs2WlJib2ttb1JuOFBqRGhZWA==");
//            httpGet.addHeader("sec-ch-ua", "\"Chromium\";v=\"110\", \"Not A(Brand\";v=\"24\", \"Microsoft Edge\";v=\"110\"");
//            httpGet.addHeader("sec-ch-ua-mobile", "?0");
//            httpGet.addHeader("sec-ch-ua-platform", "\"Windows\"");
//            httpGet.addHeader("DNT", "1");
//            httpGet.addHeader("Upgrade-Insecure-Requests", "1");
//            httpGet.addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.46");
//            httpGet.addHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
//            httpGet.addHeader("Sec-Fetch-Site", "none");
//            httpGet.addHeader("Sec-Fetch-Mode", "navigate");
//            httpGet.addHeader("Sec-Fetch-User", "?1");
//            httpGet.addHeader("Sec-Fetch-Dest", "document");
//            httpGet.addHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br");
//            httpGet.addHeader(HttpHeaders.ACCEPT_LANGUAGE, "es-419,es;q=0.9,es-ES;q=0.8,en;q=0.7,en-GB;q=0.6,en-US;q=0.5");

            System.out.println("Executing request " + httpGet.getMethod() + " " + httpGet.getUri());

            String jsonResponseString = httpclient.execute(httpGet, response -> {
                System.out.println("----------------------------------------");
                System.out.println(httpGet + " -> " + new StatusLine(response));

                String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
//                System.out.println(responseString);

                EntityUtils.consume(response.getEntity());

                return responseString;
            });

            System.out.println(jsonResponseString);

        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        } catch (URISyntaxException urise) {
            throw new RuntimeException(urise);
        }
    }


}