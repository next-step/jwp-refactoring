package kitchenpos.menu.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.acceptance.RestAssuredApi;
import kitchenpos.menu.domain.dto.ProductRequest;
import kitchenpos.menu.domain.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("상품 인수 테스트")
public class ProductAcceptanceTest extends AcceptanceTest {

    private ProductRequest 매콤치킨;
    private ProductRequest 치즈볼;
    private ProductRequest 사이다;

    @BeforeEach
    public void setUp() {
        super.setUp();

        매콤치킨 = ProductRequest.of("매콤치킨", BigDecimal.valueOf(13000));
        치즈볼 = ProductRequest.of("치즈볼", BigDecimal.valueOf(2000));
        사이다 = ProductRequest.of("사이다", BigDecimal.valueOf(1000));
    }

    @Test
    @DisplayName("상품 기능 정상 시나리오")
    void normalScenario() {
        상품_등록됨(상품_등록_요청(매콤치킨));
        상품_등록됨(상품_등록_요청(치즈볼));
        상품_등록됨(상품_등록_요청(사이다));

        ExtractableResponse<Response> response = 상품_목록_조회_요청();
        상품_목록_조회됨(response);
        상품_목록_일치됨(response, Arrays.asList("매콤치킨", "치즈볼", "사이다"));
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
}
