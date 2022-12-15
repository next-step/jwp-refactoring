package kitchenpos.product.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductAcceptance extends AcceptanceTest {

    @DisplayName("상품을 생성한다")
    @Test
    void createProduct() {
        ExtractableResponse<Response> response = 상품_생성을_요청("스테이크", new BigDecimal(25000));

        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
    }

    @DisplayName("상품가격이 0보다 작으면 상품생성에 실패")
    @Test
    void createProductWithNegativePrice() {
        ExtractableResponse<Response> response = 상품_생성을_요청("스테이크", new BigDecimal(-25000));

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    @DisplayName("상품 목록을 조회한다")
    @Test
    void list() {
        상품_생성을_요청("스테이크", new BigDecimal(25000));
        상품_생성을_요청("스파게티", new BigDecimal(18000));

        ExtractableResponse<Response> response = 상품_목록을_요청();

        assertThat(response.jsonPath().getList(".", ProductResponse.class)).hasSize(2);
    }

    public static ExtractableResponse<Response> 상품_생성을_요청(String name, BigDecimal price) {
        ProductRequest request = new ProductRequest(name, price);

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 상품_목록을_요청() {
        return RestAssured.given().log().all()
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }
}
