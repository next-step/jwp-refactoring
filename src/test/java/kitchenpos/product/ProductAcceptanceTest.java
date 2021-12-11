package kitchenpos.product;

import static kitchenpos.product.step.ProductAcceptanceStep.상품_등록_되어_있음;
import static kitchenpos.product.step.ProductAcceptanceStep.상품_등록_됨;
import static kitchenpos.product.step.ProductAcceptanceStep.상품_등록_요청;
import static kitchenpos.product.step.ProductAcceptanceStep.상품_목록_조회_됨;
import static kitchenpos.product.step.ProductAcceptanceStep.상품_목록_조회_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.ui.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("상품 관련 기능")
class ProductAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("상품을 등록할 수 있다.")
    void create() {
        //given
        String name = "후라이드치킨";
        BigDecimal price = BigDecimal.TEN;

        //when
        ExtractableResponse<Response> response = 상품_등록_요청(name, price);

        //then
        상품_등록_됨(response, name, price);
    }

    @Test
    @DisplayName("상품들을 조회할 수 있다.")
    void list() {
        //given
        String name = "후라이드치킨";
        BigDecimal price = BigDecimal.TEN;
        ProductResponse product = 상품_등록_되어_있음(name, price);

        //when
        ExtractableResponse<Response> response = 상품_목록_조회_요청();

        //then
        상품_목록_조회_됨(response, product);
    }
}
