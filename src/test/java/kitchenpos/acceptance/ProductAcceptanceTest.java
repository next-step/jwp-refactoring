package kitchenpos.acceptance;

import static kitchenpos.acceptance.ProductRestAssured.상품_등록_요청;
import static kitchenpos.acceptance.ProductRestAssured.상품_목록_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("상품 관련 기능")
class ProductAcceptanceTest extends AcceptanceTest {
    Product 피자;
    Product 스파게티;

    @BeforeEach
    public void setUp() {
        super.setUp();

        피자 = Product.of(1L, "피자", BigDecimal.valueOf(20000L));
        스파게티 = Product.of(2L, "스파게티", BigDecimal.valueOf(20000L));
    }

    /**
     * When 상품을 등록 요청하면
     * Then 상품이 등록 됨
     */
    @DisplayName("상품을 등록한다.")
    @Test
    void create() {
        // when
        ExtractableResponse<Response> response = 상품_등록_요청(피자);

        // then
        상품_등록됨(response);
    }

    /**
     * When 상품 가격이 0원 아래인 상품을 등록 요청하면
     * Then 상품이 등록 실패 됨
     */
    @DisplayName("상품 가격이 0원 아래인 상품을 등록 요청하면 실패한다.")
    @Test
    void createWithPriceUnderZero() {
        // when
        ExtractableResponse<Response> response = 상품_등록_요청(Product.of(1L, "스파게티", BigDecimal.valueOf(-100L)));

        // then
        상품_등록_실패됨(response);
    }

    /**
     * Given 상품을 등록하고
     * When 상품 목록을 조회 하면
     * Then 상품 목록 조회 됨
     */
    @DisplayName("상품 목록을 조회 한다.")
    @Test
    void getProducts() {
        // given
        Product 등록한_피자 = 상품_등록_요청(피자).as(Product.class);
        Product 등록한_스파게티 = 상품_등록_요청(스파게티).as(Product.class);

        // when
        ExtractableResponse<Response> response = 상품_목록_조회_요청();

        // then
        상품_목록_조회됨(response, Lists.newArrayList(등록한_피자, 등록한_스파게티));
    }

    private void 상품_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 상품_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 상품_목록_조회됨(ExtractableResponse<Response> response, List<Product> expectedProducts) {
        List<Product> products = response.jsonPath().getList(".", Product.class);
        assertThat(products).containsExactlyElementsOf(expectedProducts);
    }
}