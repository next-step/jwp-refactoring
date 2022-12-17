package kitchenpos.product.acceptance;

import static kitchenpos.product.acceptance.ProductAcceptanceTestFixture.상품_등록_되어_있음;
import static kitchenpos.product.acceptance.ProductAcceptanceTestFixture.상품_목록_조회_요청;
import static kitchenpos.product.acceptance.ProductAcceptanceTestFixture.상품_목록_조회됨;
import static kitchenpos.product.acceptance.ProductAcceptanceTestFixture.상품_목록_포함됨;
import static kitchenpos.product.acceptance.ProductAcceptanceTestFixture.상품_생성_요청;
import static kitchenpos.product.acceptance.ProductAcceptanceTestFixture.상품_생성됨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.product.dto.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("상품 관련 기능")
public class ProductAcceptanceTest extends AcceptanceTest {
    private ProductRequest 후라이드치킨;
    private ProductRequest 양념치킨;

    @BeforeEach
    void productSetUp() {
        super.setUp();

        후라이드치킨 = new ProductRequest("후라이드치킨", BigDecimal.valueOf(15000));
        양념치킨 = new ProductRequest("양념치킨", BigDecimal.valueOf(18000));
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        ExtractableResponse<Response> response = 상품_생성_요청(후라이드치킨);

        상품_생성됨(response);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void getProductList() {
        ExtractableResponse<Response> createResponse1 = 상품_등록_되어_있음(후라이드치킨);
        ExtractableResponse<Response> createResponse2 = 상품_등록_되어_있음(양념치킨);

        ExtractableResponse<Response> response = 상품_목록_조회_요청();

        상품_목록_조회됨(response);
        상품_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }
}
