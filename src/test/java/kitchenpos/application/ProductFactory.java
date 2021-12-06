package kitchenpos.application;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Product;
import org.springframework.http.MediaType;

public class ProductFactory {

    public static ExtractableResponse<Response> 상품_생성_요청(Product params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/products")
                .then().log().all().
                extract();
    }

    public static ExtractableResponse<Response> 상품_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/products")
                .then().log().all().
                extract();
    }
}
