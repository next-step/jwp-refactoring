package kitchenpos.product.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import product.dto.ProductRequest;

public class ProductAcceptanceUtils {
    private ProductAcceptanceUtils() {
    }

    public static ExtractableResponse<Response> 상품_등록되어_있음(ProductRequest request) {
        return 상품_생성_요청(request);
    }

    public static ExtractableResponse<Response> 상품_생성_요청(ProductRequest request) {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }
}
