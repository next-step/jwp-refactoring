package kitchenpos.menu.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("상품 관련 기능 인수 테스트")
public class ProductAcceptanceTest extends AcceptanceTest {

    /**
     * When 상품 생성을 요청하면
     * Then 상품이 생성된다.
     */
    @DisplayName("상품을 생성한다.")
    @Test
    void createProduct() {
        // when
        ExtractableResponse<Response> response =
                상품_생성_요청("스테이크", new BigDecimal(25000));

        // then
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
    }

    /**
     * When 상품의 이름을 빈 값으로 하여 상품 생성을 요청하면
     * Then 상품을 생성할 수 없다.
     */
    @DisplayName("빈 값의 이름을 입력하여 상품을 생성한다.")
    @Test
    void createProductWithNullName() {
        // when
        ExtractableResponse<Response> response =
                상품_생성_요청(null, new BigDecimal(25000));

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * When 상품의 가격을 빈 값으로 하여 상품 생성을 요청하면
     * Then 상품을 생성할 수 없다.
     */
    @DisplayName("빈 값의 가격을 입력하여 상품을 생성한다.")
    @Test
    void createProductWithNullPrice() {
        // when
        ExtractableResponse<Response> response =
                상품_생성_요청("스테이크", null);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * When 상품의 가격을 음수 값으로 하여 상품 생성을 요청하면
     * Then 상품을 생성할 수 없다.
     */
    @DisplayName("음수 값의 가격을 입력하여 상품을 생성한다.")
    @Test
    void createProductWithNegativePrice() {
        // when
        ExtractableResponse<Response> response =
                상품_생성_요청("스테이크", new BigDecimal(-25000));

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 상품을 생성하고
     * When 상품 목록을 조회하면
     * Then 상품 목록이 조회된다.
     */
    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        // given
        상품_생성_요청("스테이크", new BigDecimal(25000));
        상품_생성_요청("스파게티", new BigDecimal(18000));

        // when
        ExtractableResponse<Response> response = product_list_has_been_queried();

        // then
        assertThat(response.jsonPath().getList(".", ProductResponse.class)).hasSize(2);
    }

    public static ExtractableResponse<Response> 상품_생성_요청(String name, BigDecimal price) {
        ProductRequest request = new ProductRequest(name, price);

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> product_list_has_been_queried() {
        return RestAssured.given().log().all()
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }
}
