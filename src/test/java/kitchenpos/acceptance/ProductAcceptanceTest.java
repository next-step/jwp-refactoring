package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static kitchenpos.fixture.RestAssuredFixture.*;

@DisplayName("상품 관련 기능")
class ProductAcceptanceTest extends AcceptanceTest {

    private static final String API_URL = "/api/products";

    @DisplayName("상품을 관리한다.")
    @Test
    void manageProduct() {
        // given
        Product product = new Product();
        product.setName("강정치킨");
        product.setPrice(BigDecimal.valueOf(17_000));

        // when
        ExtractableResponse<Response> 상품_생성_응답 = 상품_생성_요청(product);
        // then
        상품_생성됨(상품_생성_응답);

        // when
        ExtractableResponse<Response> 상품_목록_조회_응답 = 상품_목록_조회_요청();
        // then
        상품_목록_조회됨(상품_목록_조회_응답);
    }

    private static ExtractableResponse<Response> 상품_생성_요청(Product params) {
        return 생성_요청(API_URL, params);
    }

    private void 상품_생성됨(ExtractableResponse<Response> response) {
        생성됨_201_CREATED(response);
    }

    private ExtractableResponse<Response> 상품_목록_조회_요청() {
        return 목록_조회_요청(API_URL);
    }

    private void 상품_목록_조회됨(ExtractableResponse<Response> response) {
        성공_200_OK(response);
    }

    public static Product 상품_등록되어_있음(String name, long price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));

        return 상품_생성_요청(product).as(Product.class);
    }
}
