package kitchenpos.helper.AcceptanceApiHelper;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Product;
import org.springframework.http.MediaType;

public class ProductApiHelper {

    public static ExtractableResponse<Response> 상품_등록하기(Product 상품정보) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(상품정보)
            .when().post("/api/products")
            .then().log().all().
            extract();
    }

    public static ExtractableResponse<Response> 상품_리스트_조회하기() {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/products")
            .then().log().all().
            extract();
    }

}
