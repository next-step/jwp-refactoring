package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ProductRestControllerTest extends BaseTest {
    @Test
    void 생성() {
        ResponseEntity<Product> response = 상품_생성_요청(new Product("후라이드", BigDecimal.valueOf(16000)));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void 조회() {
        상품_생성_요청(new Product("후라이드", BigDecimal.valueOf(16000)));
        상품_생성_요청(new Product("양념치킨", BigDecimal.valueOf(17000)));

        ResponseEntity<List<Product>> response = 상품_조회_요청();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(2);
    }

    public static ResponseEntity<Product> 상품_생성_요청(Product product) {
        return testRestTemplate.postForEntity(basePath + "/api/products", product, Product.class);
    }

    private ResponseEntity<List<Product>> 상품_조회_요청() {
        return testRestTemplate.exchange(
                basePath + "/api/products",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Product>>() {});
    }
}
