package kitchenpos.menu.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.acceptance.RestAssuredApi;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("상품 인수 테스트")
public class ProductAcceptanceTest extends AcceptanceTest {
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    @DisplayName("상품 정상 시나리오")
    void normalScenario() {
        ProductRequest 매콤치킨 = ProductRequest.of("매콤치킨", BigDecimal.valueOf(13000));
        ProductRequest 치즈볼 = ProductRequest.of("치즈볼", BigDecimal.valueOf(2000));
        ProductRequest 사이다 = ProductRequest.of("사이다", BigDecimal.valueOf(1000));

        상품_등록됨(상품_등록_요청(매콤치킨));
        상품_등록됨(상품_등록_요청(치즈볼));
        상품_등록됨(상품_등록_요청(사이다));

        ExtractableResponse<Response> response = 상품_목록_조회_요청();
        상품_목록_조회됨(response);
        상품_목록_일치됨(response, Arrays.asList("매콤치킨", "치즈볼", "사이다"));
    }

    @Test
    @DisplayName("상품 예외 시나리오")
    void exceptionScenario() {
        ProductRequest 이름없는상품 = ProductRequest.of(null, BigDecimal.valueOf(13000));
        ProductRequest 가격없는상품 = ProductRequest.of("매콤치킨", null);

        상품_등록_실패됨(상품_등록_요청(이름없는상품));
        상품_등록_실패됨(상품_등록_요청(가격없는상품));

        ExtractableResponse<Response> response = 상품_목록_조회_요청();
        상품_목록_조회됨(response);
        상품_목록_일치됨(response, Collections.emptyList());
    }

    public static ExtractableResponse<Response> 상품_등록_요청(ProductRequest request) {
        return RestAssuredApi.post("/api/products", request);
    }

    private ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssuredApi.get("/api/products");
    }

    private void 상품_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 상품_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 상품_목록_일치됨(ExtractableResponse<Response> response, List<String> excepted) {
        assertThat(response.jsonPath().getList(".", ProductResponse.class))
                .extracting("name")
                .isEqualTo(excepted);
    }

    public static void 상품_등록_실패됨(ExtractableResponse<Response> response) {
        AssertionsForClassTypes.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
