package kitchenpos.product.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;

public class ProductAcceptanceTest {

    @DisplayName("상품을 생성한다.")
    @Test
    void createProduct() {
        // given
        String name = "후라이드치킨";
        int price = 15_000;

        // when
        ExtractableResponse<Response> response = 상품_생성_요청(name, price);

        // then
        상품_생성_요청_됨(response, name, price);
    }

    @DisplayName("가격은 0 이상이여야 한다.")
    @Test
    void createProduct1() {
        // when
        ExtractableResponse<Response> response = 상품_생성_요청("후라이드치킨", -15_000);

        // then
        상품_생성_요청_실패(response);
    }

    public static ExtractableResponse<Response> 상품_생성_요청(String name, int price) {
        ProductRequest productRequest = new ProductRequest(name, BigDecimal.valueOf(price));

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 상품_생성되어_있음(String name, int price) {
        return 상품_생성_요청(name, price);
    }

    private static void 상품_생성_요청_됨(ExtractableResponse<Response> response, String name, int price) {
        ProductResponse productResponse = response.as(ProductResponse.class);
        assertAll(
                () -> Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> Assertions.assertThat(productResponse.getName()).isEqualTo(name),
                () -> Assertions.assertThat(productResponse.getPrice()).isEqualTo(BigDecimal.valueOf(price))
        );
    }

    private static void 상품_생성_요청_실패(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
