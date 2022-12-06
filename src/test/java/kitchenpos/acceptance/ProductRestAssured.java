package kitchenpos.acceptance;

import static kitchenpos.domain.ProductTestFixture.generateProduct;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import kitchenpos.domain.Product;
import org.springframework.http.MediaType;

public class ProductRestAssured {

    public static ExtractableResponse<Response> 상품_등록되어_있음(Long id, String name, BigDecimal price) {
        return 상품_생성_요청(generateProduct(id, name, price));
    }

    public static ExtractableResponse<Response> 상품_등록되어_있음(Product product) {
        return 상품_생성_요청(product);
    }

    public static ExtractableResponse<Response> 상품_생성_요청(Product product) {
        return RestAssured
                .given().log().all()
                .body(product)
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
