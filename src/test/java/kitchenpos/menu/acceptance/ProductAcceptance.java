package kitchenpos.menu.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.ProductRequest;
import org.springframework.http.MediaType;

public class ProductAcceptance {
    public static ExtractableResponse<Response> create_product(String name, Integer price) {
        ProductRequest request = new ProductRequest(name, price);

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> product_list_has_been_queried() {
        return RestAssured.given().log().all()
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }
}
