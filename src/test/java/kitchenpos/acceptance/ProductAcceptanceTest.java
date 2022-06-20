package kitchenpos.acceptance;

import static kitchenpos.acceptance.support.ProductAcceptanceSupport.상품_등록됨;
import static kitchenpos.acceptance.support.ProductAcceptanceSupport.상품_등록요청;
import static kitchenpos.acceptance.support.ProductAcceptanceSupport.상품목록_조회됨;
import static kitchenpos.acceptance.support.ProductAcceptanceSupport.상품목록_조회요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("상품에 관한 인수 테스트")
class ProductAcceptanceTest extends AcceptanceTest {
    private Product 상품;
    private Product 상품2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        상품 = Product.of(null, "후라이드치킨", BigDecimal.valueOf(15000L));
        상품2 = Product.of(null, "불고기피자", BigDecimal.valueOf(13000L));
    }

    @DisplayName("상품을 등록한다")
    @Test
    void create_test() {
        // when
        ExtractableResponse<Response> response = 상품_등록요청(상품);

        // then
        상품_등록됨(response);
    }

    @DisplayName("상품목록을 조회한다")
    @Test
    void find_test() {
        // given
        상품_등록요청(상품);
        상품_등록요청(상품2);

        // when
        ExtractableResponse<Response> response = 상품목록_조회요청();

        // then
        상품목록_조회됨(response, 2);
    }
}
