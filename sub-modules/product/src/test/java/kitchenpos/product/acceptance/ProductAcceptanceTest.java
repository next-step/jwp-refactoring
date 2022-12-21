package kitchenpos.product.acceptance;


import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.ProductAcceptanceConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.product.fixture.ProductTestFixture.*;

@DisplayName("상품 관련 인수 테스트")
public class ProductAcceptanceTest extends ProductAcceptanceConfig {

    /**
     * When : 상품 생성을 요청하면
     * Then : 상품이 생성 된다.
     */
    @DisplayName("상품을 생성 한다")
    @Test
    void createProduct() {
        // when
        ExtractableResponse<Response> response = 상품_생성_요청("순살치킨", 9_000);

        // then
        상품_생성됨(response);
    }

    /**
     * When : 가격이 0 이하인 상품을 생성하면
     * Then : 상품 생성이 실패한다
     */
    @DisplayName("상품을 생성 실패한다")
    @Test
    void createProductFailed() {
        // when
        ExtractableResponse<Response> response = 상품_생성_요청("순살치킨", -1);

        // then
        상품_생성_실패됨(response);
    }

    /**
     * Given : 상품이 생성되어 있다
     * When : 상품 목록 조회를 요청하면
     * Then : 상품 목록을 응답한다.
     */
    @DisplayName("상품 목록을 조회한다")
    @Test
    void findProducts() {
        // given
        상품_생성_요청("신제품치킨", 20_000);

        // when
        ExtractableResponse<Response> response = 상품_목록_조회();

        // then
        상품_목록_조회됨(response);
    }
}
