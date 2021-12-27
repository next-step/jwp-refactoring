package kitchenpos.product.acceptance;

import static kitchenpos.product.acceptance.step.ProductAcceptanceStep.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.dto.ProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("상품 관리 기능")
class ProductAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("상품을 관리한다.")
    void 상품_관리() {
        // given
        ProductRequest 양념치킨 = new ProductRequest("양념치킨", BigDecimal.valueOf(16000));

        // when
        ExtractableResponse<Response> 상품등록_결과 = 상품_등록_요청(양념치킨);
        // then
        Long 등록된_상품번호 = 상품등록_검증(상품등록_결과, 양념치킨);

        // then
        ExtractableResponse<Response> 상품_목록조회_결과 = 상품_목록조회_요청();
        // when
        상품_목록조회_검증(상품_목록조회_결과, 등록된_상품번호);
    }
}
