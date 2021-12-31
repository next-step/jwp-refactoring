package kitchenpos.product.ui;

import static kitchenpos.product.ui.ProductAcceptanceTestHelper.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;

class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품을 관리한다")
    @Test
    void createProduct() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "강정치킨");
        params.put("price", "17000");

        // when
        ExtractableResponse<Response> createResponse = 상품_생성_요청(params);

        // then
        상품_생성됨(createResponse);

        // given
        int expectedLength = 7;

        // when
        ExtractableResponse<Response> getResponse = 상품_조회_요청();

        // then
        상품_조회됨(getResponse);
        상품갯수_예상과_일치(getResponse, expectedLength);
    }

    @DisplayName("가격이 없거나 0보다 작을시 상품 생성 불가능")
    @Test
    void createProductWithPriceLessThanZero() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "강정치킨");
        params.put("price", "-1");

        // when
        ExtractableResponse<Response> createResponse = 상품_생성_요청(params);

        // then
        상품_생성_실패됨(createResponse);
    }

    @DisplayName("가격이 없거나 0보다 작을시 상품 생성 불가능")
    @Test
    void createProductWithPriceNull() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "강정치킨");

        // when
        ExtractableResponse<Response> createResponse = 상품_생성_요청(params);

        // then
        상품_생성_실패됨(createResponse);
    }
}