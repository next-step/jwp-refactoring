package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static kitchenpos.acceptance.ProductAcceptanceFactory.상품_등록_실패;
import static kitchenpos.acceptance.ProductAcceptanceFactory.상품_등록_요청;
import static kitchenpos.acceptance.ProductAcceptanceFactory.상품_조회_성공;
import static kitchenpos.acceptance.ProductAcceptanceFactory.상품_조회_요청;

@DisplayName("상품 관련")
public class ProductAcceptanceTest extends AcceptanceTest {

    @Test
    void 상품_등록_성공() {
        ExtractableResponse<Response> 상품등록_결과 = 상품_등록_요청("후라이드", 16000);

        ProductAcceptanceFactory.상품_등록_성공(상품등록_결과, "후라이드");
    }

    @Test
    void 상품_등록_실패_상품가격_음수() {
        ExtractableResponse<Response> 상품등록_결과 = 상품_등록_요청("후라이드", -16000);

        상품_등록_실패(상품등록_결과);
    }

    @Test
    void 상품_조회() {
        Product 후라이드 = 상품_등록_요청("후라이드", 16000).as(Product.class);
        Product 양념치킨 = 상품_등록_요청("양념치킨", 16000).as(Product.class);
        Product 간장치킨 = 상품_등록_요청("간장치킨", 17000).as(Product.class);

        ExtractableResponse<Response> 상품조회_결과 = 상품_조회_요청();

        상품_조회_성공(상품조회_결과, Arrays.asList(후라이드, 양념치킨, 간장치킨));
    }

}
