package kitchenpos.product.acceptance;

import static kitchenpos.product.acceptance.ProductAcceptanceTestHelper.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;

@DisplayName("상품 관련 기능")
public class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품을 등록한다.")
    @Test
    void create() {
        ExtractableResponse<Response> 상품_등록_요청_응답 = 상품_등록_요청("제육볶음", 8900);

        상품_등록_요청됨(상품_등록_요청_응답);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        ExtractableResponse<Response> 상품_목록_요청_응답 = 상품_목록_요청();

        상품_목록_요청됨(상품_목록_요청_응답);
    }
}
