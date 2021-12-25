package kitchenpos.product.testfixtures.acceptance;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.common.CustomTestRestTemplate;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class ProductAcceptanceFixtures {

    private static final String BASE_URL = "/api/products";

    public static ResponseEntity<List<ProductResponse>> 상품_전체_조회_요청() {
        TestRestTemplate restTemplate = CustomTestRestTemplate.initInstance();
        return restTemplate.exchange(BASE_URL, HttpMethod.GET, null,
            new ParameterizedTypeReference<List<ProductResponse>>() {
            });
    }

    public static ResponseEntity<ProductResponse> 상품_생성_요청(ProductRequest productRequest) {
        TestRestTemplate restTemplate = CustomTestRestTemplate.initInstance();
        return restTemplate.postForEntity(BASE_URL, productRequest, ProductResponse.class);
    }

    public static ProductRequest 상품_정의(String name, BigDecimal price) {
        return new ProductRequest(name, price);
    }
}
