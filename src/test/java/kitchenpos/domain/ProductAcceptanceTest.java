package kitchenpos.domain;

import static kitchenpos.domain.ProductAcceptanceTestMethod.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import kitchenpos.AcceptanceTest;
import kitchenpos.application.fixture.ProductFixtureFactory;
import kitchenpos.product.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductAcceptanceTest extends AcceptanceTest {

    private Product 우아한_초밥_1;
    private Product 우아한_초밥_2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        우아한_초밥_1 = ProductFixtureFactory.create("우아한_초밥_1", BigDecimal.valueOf(10_000));
        우아한_초밥_2 = ProductFixtureFactory.create("우아한_초밥_2", BigDecimal.valueOf(20_000));
    }

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void create01() {
        // when
        ExtractableResponse<Response> response = 상품_등록_요청(우아한_초밥_1);

        // then
        상품_등록됨(response);
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void find01() {
        // given
        ExtractableResponse<Response> createdResponse1 = 상품_등록되어_있음(우아한_초밥_1);
        ExtractableResponse<Response> createdResponse2 = 상품_등록되어_있음(우아한_초밥_2);

        // when
        ExtractableResponse<Response> response = 상품_목록_조회_요청();

        // then
        상품_목록_응답됨(response);
        상품_목록_포함됨(response, Lists.newArrayList(createdResponse1, createdResponse2));
    }
}