package kitchenpos.product.acceptance;

import static kitchenpos.product.acceptance.ProductAcceptanceUtils.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;

@DisplayName("상품 테이블 인수 테스트")
public class ProductAcceptanceTest extends AcceptanceTest {
    /**
     * when 상품 등록 요청
     * then 상품 등록 완료
     * when 상품 목록 조회 요청
     * then 상품 목록 조회 완료
     */
    @DisplayName("상품 관리")
    @Test
    void product() {
        // given
        String name = "상품";
        long price = 17000;
        // when
        ExtractableResponse<Response> 상품_등록_요청 = 상품_등록_요청(name, price);
        // then
        상품_등록_완료(상품_등록_요청);
        // when
        ExtractableResponse<Response> 상품_목록_조회_요청 = 상품_목록_조회_요청();
        // then
        상품_목록_조회_완료(상품_목록_조회_요청);
        assertThat(상품_목록_이름_추출(상품_목록_조회_요청)).contains(name);
        assertThat(상품_목록_가격_추출(상품_목록_조회_요청)).contains(price);
    }
}
