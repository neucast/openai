package mx.org.neucast.openai.test;

public class TestSkipVerification {

    public static void main(String[] args) {
        SkipVerification skipVerification = new SkipVerification("52.152.96.252","443");
        skipVerification.run();
    }
}
