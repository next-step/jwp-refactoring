package kitchenpos.product.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@DisplayName("상품 관련 기능 인수테스트")
public class ProductAcceptanceTest extends AcceptanceTest {
    /**
     * Feature 상품 관련 기능
     *
     * Scenario 상품 관련 기능
     * Given 0원 미만 가격
     * When 상품 등록 요청
     * Then 상품 등록 실패됨
     *
     * When 상품 등록 요청
     * Then 상품 등록됨
     * When 상품 목록 조회 요청
     * Then 상품 목록 조회됨
     */
    @Test
    @DisplayName("상품 등록 관련 기능")
    void integrationTest() {
        //when
        ResponseEntity<Product> 가격_0원_미만_상품_등록_응답_결과 = 상품_등록_요청("허니콤보", -1L);
        //then
        상품_등록_실패됨(가격_0원_미만_상품_등록_응답_결과);

        //when
        ResponseEntity<Product> 상품_등록_응답_결과 = 상품_등록_요청("허니콤보", 20_000L);
        //then
        상품_등록됨(상품_등록_응답_결과);

        //when
        ResponseEntity<List<Product>> 상품_조회_요청_응답_결과 = 상품_조회_요청();
        //then
        상품_목록_조회됨(상품_조회_요청_응답_결과, "허니콤보");
    }

    public static ResponseEntity<Product> 상품_등록_요청(String name, long price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return testRestTemplate.postForEntity("/api/products", product, Product.class);
    }

    public static ResponseEntity<List<Product>> 상품_조회_요청() {
        return testRestTemplate.exchange("/api/products", HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Product>>() {});
    }

    private void 상품_목록_조회됨(ResponseEntity<List<Product>> response, String... names) {
        List<String> productNames = 상품_이름_목록_조회(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(productNames).containsExactly(names);
    }

    private List<String> 상품_이름_목록_조회(ResponseEntity<List<Product>> response) {
        return response.getBody().stream()
                .map(Product::getName)
                .collect(Collectors.toList());
    }

    private void 상품_등록됨(ResponseEntity<Product> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().get("Location")).isNotNull();
    }

    private void 상품_등록_실패됨(ResponseEntity<Product> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
