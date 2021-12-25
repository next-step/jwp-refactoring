package kitchenpos.product.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static kitchenpos.product.acceptance.step.ProductAcceptStep.상품_등록_요청;
import static kitchenpos.product.acceptance.step.ProductAcceptStep.상품_등록_확인;
import static kitchenpos.product.acceptance.step.ProductAcceptStep.상품_목록_조회_요청;
import static kitchenpos.product.acceptance.step.ProductAcceptStep.상품_목록_조회_확인;

@DisplayName("상품 인수테스트")
class ProductAcceptTest extends AcceptanceTest {
    @DisplayName("상품을 관리한다")
    @Test
    void 상품을_관리한다() {
        // given
        ProductRequest 상품_등록_요청_데이터 = ProductRequest.of("강정치킨", BigDecimal.valueOf(17_000));

        // when
        ExtractableResponse<Response> 상품_등록_응답 = 상품_등록_요청(상품_등록_요청_데이터);

        // then
        ProductResponse 등록된_상품 = 상품_등록_확인(상품_등록_응답, 상품_등록_요청_데이터);

        // when
        ExtractableResponse<Response> 상품_목록_조회_응답 = 상품_목록_조회_요청();

        // then
        상품_목록_조회_확인(상품_목록_조회_응답, 등록된_상품);
    }
}
