package kitchenpos.product.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.RestAssuredFixture;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

@DisplayName("상품 관련 기능")
public class ProductAcceptanceTest extends AcceptanceTest {

    private static final String API_URL = "/api/products";

    @DisplayName("상품을 관리한다.")
    @Test
    void manageProduct() {
        // given
        ProductRequest productRequest = ProductRequest.of("강정치킨", BigDecimal.valueOf(17_000));

        // when
        ExtractableResponse<Response> 상품_생성_응답 = 상품_생성_요청(productRequest);
        // then
        상품_생성됨(상품_생성_응답);

        // when
        ExtractableResponse<Response> 상품_목록_조회_응답 = 상품_목록_조회_요청();
        // then
        상품_목록_조회됨(상품_목록_조회_응답);
    }

    private static ExtractableResponse<Response> 상품_생성_요청(ProductRequest params) {
        return RestAssuredFixture.생성_요청(API_URL, params);
    }

    private void 상품_생성됨(ExtractableResponse<Response> response) {
        RestAssuredFixture.생성됨_201_CREATED(response);
    }

    private ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssuredFixture.목록_조회_요청(API_URL);
    }

    private void 상품_목록_조회됨(ExtractableResponse<Response> response) {
        RestAssuredFixture.성공_200_OK(response);
    }

    public static ProductResponse 상품_등록되어_있음(String name, long price) {
        ProductRequest productRequest = ProductRequest.of(name, BigDecimal.valueOf(price));
        return 상품_생성_요청(productRequest).as(ProductResponse.class);
    }
}
