package kitchenpos.product.acceptance;

import static kitchenpos.product.domain.ProductTestFixture.generateProductRequest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import kitchenpos.product.dto.ProductRequest;
import org.springframework.http.MediaType;

public class ProductRestAssured {

    public static ExtractableResponse<Response> 상품_등록되어_있음(String name, BigDecimal price) {
        return 상품_생성_요청(generateProductRequest(name, price));
    }

    public static ExtractableResponse<Response> 상품_등록되어_있음(ProductRequest productRequest) {
        return 상품_생성_요청(productRequest);
    }

    public static ExtractableResponse<Response> 상품_생성_요청(ProductRequest productRequest) {
        return RestAssured
                .given().log().all()
                .body(productRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
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
