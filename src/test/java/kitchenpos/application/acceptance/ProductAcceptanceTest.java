package kitchenpos.application.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.application.acceptance.BaseAcceptanceTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("상품 관련 기능")
class ProductAcceptanceTest extends BaseAcceptanceTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    /**
     * Given 상품이름과 가격 정보를 이용하여
     * When 상품을 생성하면
     * Then 생성된 상품 데이터가 리턴된다
     */
    @DisplayName("상품 생성")
    @Test
    void 상품_생성_요청() {
        // given
        String name = "녹두빈대떡";
        BigDecimal price = new BigDecimal(7000);

        // when
        ExtractableResponse<Response> 상품_생성_요청_응답 = 상품_생성_요청(name, price);

        // then
        assertAll(
                () -> assertEquals(HttpStatus.CREATED.value(), 상품_생성_요청_응답.statusCode()),
                () -> assertNotNull(상품_생성_요청_응답.as(Product.class).getId()),
                () -> assertEquals(name, 상품_생성_요청_응답.as(Product.class).getName()),
                () -> assertEquals(0, price.compareTo(상품_생성_요청_응답.as(Product.class).getPrice()))
        );
    }

    /**
     * Given 상품의 가격을 0원 미만으로 지정하여
     * When 상품을 생성하면
     * Then 오류가 발생한다
     */
    @DisplayName("상품 가격이 0원 미만인 상품을 등록한다.")
    @Test
    void createProductWithInvalidPrice() {
        // given
        String name = "녹두빈대떡";
        BigDecimal price = new BigDecimal(-7000);

        // when
        ExtractableResponse<Response> 상품_생성_요청_응답 = 상품_생성_요청(name, price);

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), 상품_생성_요청_응답.statusCode());
    }

    /**
     * Given 상품 1건을 등록한 후
     * When 상품 목록을 조회하면
     * Then 1건이 조회된다
     */
    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        // given
        상품_생성_요청("녹두빈대떡", new BigDecimal(7000));

        // when
        ExtractableResponse<Response> 상품_목록_조회_요청_응답 = 상품_목록_조회_요청();

        // then
        assertThat(상품_목록_조회_요청_응답.jsonPath().getList("")).hasSize(1);
    }

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 상품_생성_요청(String name, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);

        return RestAssured
                .given().log().all()
                .body(product)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }
}
