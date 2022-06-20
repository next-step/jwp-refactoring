package kitchenpos.product.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("상품 관련 기능 인수테스트")
public class ProductAcceptanceTest extends AcceptanceTest {
    /**
     * Feature 상품 관련 기능
     *
     * Scenario 상품 관련 기능
     * When 가격 0원 미만 상품 등록 요청
     * Then 상품 등록 실패됨
     * When 상품 등록 요청
     * Then 상품 등록됨
     * When 상품 목록 조회 요청
     * Then 상품 목록 조회됨
     */
    @Test
    @DisplayName("상품 등록 관련 기능")
    void createFail() {
        //when
        ExtractableResponse<Response> 가격_0원_미만_상품_등록_응답_결과 = 상품_등록_요청("허니콤보", BigDecimal.valueOf(-1L));
        //then
        상품_등록_실패됨(가격_0원_미만_상품_등록_응답_결과);

        //when
        ExtractableResponse<Response> 상품_등록_응답_결과 = 상품_등록_요청("허니콤보", BigDecimal.valueOf(20_000L));
        //then
        상품_등록됨(상품_등록_응답_결과);

        //when
        ExtractableResponse<Response> 상품_조회_요청_응답_결과 = 상품_조회_요청();
        //then
        상품_목록_조회됨(상품_조회_요청_응답_결과, "허니콤보");
    }

    private void 상품_목록_조회됨(ExtractableResponse<Response> response, String... name) {
        List<String> productNames = 상품_이름_목록_조회(response);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(productNames).containsExactly(name);
    }

    private List<String> 상품_이름_목록_조회(ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", Product.class).stream()
                .map(Product::getName)
                .collect(Collectors.toList());
    }

    private ExtractableResponse<Response> 상품_조회_요청() {
        return sendGet("/api/products");
    }

    private void 상품_등록됨(ExtractableResponse<Response> response) {
        Product productResponse = response.as(Product.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(productResponse).isNotNull();
    }

    private void 상품_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private ExtractableResponse<Response> 상품_등록_요청(String name, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return sendPost("/api/products", product);
    }
}
