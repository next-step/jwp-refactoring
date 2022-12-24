package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.product.dto.request.ProductRequest;
import kitchenpos.product.dto.response.ProductResponse;
import kitchenpos.utils.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ProductRestControllerTest extends BaseTest {
    private ProductRequest 후라이드;
    private ProductRequest 양념치킨;

    @BeforeEach
    void beforeEach() {
        후라이드 = new ProductRequest("후라이드", BigDecimal.valueOf(16000));
        양념치킨 = new ProductRequest("양념치킨", BigDecimal.valueOf(17000));
    }

    @Test
    void 생성() {
        ResponseEntity<ProductResponse> response = 상품_생성_요청(후라이드);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void 조회() {
        상품_생성_요청(후라이드);
        상품_생성_요청(양념치킨);

        ResponseEntity<List<ProductResponse>> response = 상품_조회_요청();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(2);
    }

    public static ResponseEntity<ProductResponse> 상품_생성_요청(ProductRequest productRequest) {
        return testRestTemplate.postForEntity(basePath + "/api/products", productRequest, ProductResponse.class);
    }

    private ResponseEntity<List<ProductResponse>> 상품_조회_요청() {
        return testRestTemplate.exchange(
                basePath + "/api/products",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProductResponse>>() {});
    }
}
