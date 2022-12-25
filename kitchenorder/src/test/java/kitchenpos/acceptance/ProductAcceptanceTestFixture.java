package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.http.MediaType;

public class ProductAcceptanceTestFixture {
    public static ExtractableResponse<Response> 상품_생성_요청(ProductRequest productRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    public static ProductResponse 상품_생성_되어있음(ProductRequest productRequest) {
        return 상품(상품_생성_요청(productRequest));
    }

    public static ProductResponse 상품(ExtractableResponse<Response> response) {
        return response.jsonPath().getObject("", ProductResponse.class);
    }
}
