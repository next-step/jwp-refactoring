package kitchenpos;

import kitchenpos.product.dto.ProductResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static kitchenpos.AcceptanceTest.restTemplate;
import static org.assertj.core.api.Assertions.assertThat;

public final class ProductAcceptanceTestUtil {

    private ProductAcceptanceTestUtil() {
    }

    public static ProductResponse 상품_등록됨(String name, BigDecimal price) {
        return 상품_생성_요청(name, price).getBody();
    }

    static ResponseEntity<ProductResponse> 상품_생성_요청(String name, BigDecimal price) {
        Map<String, Object> request = new HashMap<>();
        request.put("name", name);
        request.put("price", price);
        return restTemplate().postForEntity("/api/products", request, ProductResponse.class);
    }

    static ResponseEntity<List<ProductResponse>> 상품_목록_조회_요청() {
        return restTemplate().exchange("/api/products", HttpMethod.GET, null,
                                                      new ParameterizedTypeReference<List<ProductResponse>>() {});
    }

    static void 상품_생성됨(ResponseEntity<ProductResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
    }

    static void 상품_생성_실패됨(ResponseEntity<ProductResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    static void 상품_목록_응답됨(ResponseEntity<List<ProductResponse>> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    static void 상품_목록_확인됨(ResponseEntity<List<ProductResponse>> response, String... names) {
        List<String> productNames = response.getBody()
                                            .stream()
                                            .map(ProductResponse::getName)
                                            .collect(Collectors.toList());
        assertThat(productNames).containsExactly(names);
    }
}
