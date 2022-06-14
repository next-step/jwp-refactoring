package kitchenpos;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("상품 관련 기능")
public class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품 관련 기능 테스트")
    @TestFactory
    Stream<DynamicNode> product() {
        return Stream.of(
                dynamicTest("상품을 등록한다.", () -> {
                    ResponseEntity<Product> response = 상품_생성_요청("강정치킨", BigDecimal.valueOf(15_000L));

                    상품_생성됨(response);
                }),
                dynamicTest("가격이 0미만인 상품을 등록한다.", () -> {
                    ResponseEntity<Product> response = 상품_생성_요청("강정치킨", BigDecimal.valueOf(-1L));

                    상품_생성_실패됨(response);
                }),
                dynamicTest("이름이 없는 상품을 등록한다.", () -> {
                    ResponseEntity<Product> response = 상품_생성_요청(null, BigDecimal.valueOf(15_000L));

                    상품_생성_실패됨(response);
                }),
                dynamicTest("상품 목록을 조회한다.", () -> {
                    ResponseEntity<List<Product>> response = 상품_목록_조회_요청();

                    상품_목록_응답됨(response);
                    상품_목록_확인됨(response, "강정치킨");
                })
        );
    }

    public static ResponseEntity<Product> 상품_생성_요청(String name, BigDecimal price) {
        Map<String, Object> request = new HashMap<>();
        request.put("name", name);
        request.put("price", price);
        return restTemplate().postForEntity("/api/products", request, Product.class);
    }

    public static ResponseEntity<List<Product>> 상품_목록_조회_요청() {
        return restTemplate().exchange("/api/products", HttpMethod.GET, null,
                                       new ParameterizedTypeReference<List<Product>>() {});
    }

    public static void 상품_생성됨(ResponseEntity<Product> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
    }

    public static void 상품_생성_실패됨(ResponseEntity<Product> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static void 상품_목록_응답됨(ResponseEntity<List<Product>> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    public static void 상품_목록_확인됨(ResponseEntity<List<Product>> response, String... names) {
        List<String> productNames = response.getBody()
                                            .stream()
                                            .map(Product::getName)
                                            .collect(Collectors.toList());
        assertThat(productNames).containsExactly(names);
    }
}
