package mx.org.neucast.openai;

import mx.org.neucast.openai.rest.client.RESTClient;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello OpenIA!");

        String response = "";
        RESTClient restClient = new RESTClient();

        response = restClient.get("https://api.openai.com/v1/models", "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");
        System.out.println(response);

        String jsonQuery = "{\"model\": \"gpt-3.5-turbo\",\"messages\": [{\"role\": \"user\", \"content\": \"%s\"}]}".formatted("Hello!");
        response = restClient.post("https://api.openai.com/v1/chat/completions", "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", jsonQuery);
        System.out.println(response);

    }
}
