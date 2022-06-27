package kitchenpos.acceptance.product;

import static kitchenpos.acceptance.product.ProductAcceptanceTestMethod.상품_등록_요청;
import static kitchenpos.acceptance.product.ProductAcceptanceTestMethod.상품_등록되어_있음;
import static kitchenpos.acceptance.product.ProductAcceptanceTestMethod.상품_등록됨;
import static kitchenpos.acceptance.product.ProductAcceptanceTestMethod.상품_목록_응답됨;
import static kitchenpos.acceptance.product.ProductAcceptanceTestMethod.상품_목록_조회_요청;
import static kitchenpos.acceptance.product.ProductAcceptanceTestMethod.상품_목록_포함됨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import kitchenpos.AcceptanceTest;
import kitchenpos.dto.product.ProductRequest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("상품 관련 기능 인수테스트")
class ProductAcceptanceTest extends AcceptanceTest {

    private ProductRequest 우아한_초밥_1_request;
    private ProductRequest 우아한_초밥_2_request;

    @BeforeEach
    public void setUp() {
        super.setUp();

        우아한_초밥_1_request = ProductRequest.of("우아한_초밥_1", BigDecimal.valueOf(10_000));
        우아한_초밥_2_request = ProductRequest.of("우아한_초밥_2", BigDecimal.valueOf(20_000));
    }

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void create01() {
        // when
        ExtractableResponse<Response> response = 상품_등록_요청(우아한_초밥_1_request);

        // then
        상품_등록됨(response);
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void find01() {
        // given
        ExtractableResponse<Response> createdResponse1 = 상품_등록되어_있음(우아한_초밥_1_request);
        ExtractableResponse<Response> createdResponse2 = 상품_등록되어_있음(우아한_초밥_2_request);

        // when
        ExtractableResponse<Response> response = 상품_목록_조회_요청();

        // then
        상품_목록_응답됨(response);
        상품_목록_포함됨(response, Lists.newArrayList(createdResponse1, createdResponse2));
    }
}