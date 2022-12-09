package kitchenpos.acceptance;

import static kitchenpos.acceptance.ProductAcceptanceTestFixture.상품_등록_되어_있음;
import static kitchenpos.acceptance.ProductAcceptanceTestFixture.상품_목록_조회_요청;
import static kitchenpos.acceptance.ProductAcceptanceTestFixture.상품_목록_조회됨;
import static kitchenpos.acceptance.ProductAcceptanceTestFixture.상품_목록_포함됨;
import static kitchenpos.acceptance.ProductAcceptanceTestFixture.상품_생성_요청;
import static kitchenpos.acceptance.ProductAcceptanceTestFixture.상품_생성됨;
import static kitchenpos.domain.ProductFixture.createProduct;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("상품 관련 기능")
public class ProductAcceptanceTest extends AcceptanceTest {
    private Product 후라이드치킨;
    private Product 양념치킨;

    @BeforeEach
    void productSetUp() {
        super.setUp();

        후라이드치킨 = createProduct("후라이드치킨", new BigDecimal(3000));
        양념치킨 = createProduct("양념치킨", new BigDecimal(5000));
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
