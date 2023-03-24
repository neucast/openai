package mx.org.neucast.openai.test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

/**
 * @author B11160
 */
public class TestSimpleClient {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a string to search for: ");
        String searchString = scanner.nextLine();

        String input = "\"model\":\"text-davinci-001\",\"prompt\":\"%s\",\"temperature\":1,\"max_tokens\":100}".formatted(searchString);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer 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")
                .POST(HttpRequest.BodyPublishers.ofString(input))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();

        try {
            var response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
            System.out.println(response.body());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        } catch (InterruptedException ie) {
            throw new RuntimeException(ie);
        }


    }
}
