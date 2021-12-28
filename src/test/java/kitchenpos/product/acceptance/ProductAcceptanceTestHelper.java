package kitchenpos.product.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Product;

public class ProductAcceptanceTestHelper {

    public static ExtractableResponse<Response> 상품_등록되어_있음(String name, int price) {
        return 상품_등록_요청(name, price);
    }

    public static ExtractableResponse<Response> 상품_목록_요청() {
        return RestAssured
            .given().log().all()
            .when().get("/api/products")
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 상품_등록_요청(String name, int price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(product)
            .when().post("/api/products")
            .then().log().all().extract();
    }

    public static void 상품_등록_요청됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 상품_목록_요청됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
