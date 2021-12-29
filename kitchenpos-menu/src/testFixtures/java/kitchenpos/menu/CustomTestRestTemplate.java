package kitchenpos.menu;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;

public class CustomTestRestTemplate {

    private static TestRestTemplate restTemplate;

    private static int port = 65232;

    private CustomTestRestTemplate() {
    }

    public static TestRestTemplate initInstance() {
        if (restTemplate == null) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder().rootUri(
                "http://localhost:" + port);
            return new TestRestTemplate(restTemplateBuilder);
        }
        return restTemplate;
    }
}
