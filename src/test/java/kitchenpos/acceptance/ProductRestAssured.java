package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Product;
import org.springframework.http.MediaType;

public class ProductRestAssured {

    public static ExtractableResponse<Response> 상품_생성_요청(Product product) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(product)
            .when().post("/api/products")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .when().get("/api/products")
            .then().log().all()
            .extract();
    }
}
