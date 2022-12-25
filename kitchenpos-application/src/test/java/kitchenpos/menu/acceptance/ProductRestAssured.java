package kitchenpos.menu.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.ProductRequest;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductRestAssured {
    public static ExtractableResponse<Response> 상품_생성_요청(String name, BigDecimal price) {
        ProductRequest request = new ProductRequest(name, price);

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 상품_조회_요청() {
        return RestAssured.given().log().all()
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }
}
