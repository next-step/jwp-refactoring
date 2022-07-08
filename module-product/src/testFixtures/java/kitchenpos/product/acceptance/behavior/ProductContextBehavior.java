package kitchenpos.product.acceptance.behavior;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kitchenpos.product.domain.Product;

public class ProductContextBehavior {
    private ProductContextBehavior() {
    }

    public static ExtractableResponse<Response> 상품_생성_요청(String name, int price) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("price", price);

        return RestAssured
                .given().contentType(ContentType.JSON).log().all()
                .when().body(params).post("/api/products")
                .then().log().all()
                .extract();
    }

    public static Product 상품_생성됨(String name, int price) {
        return 상품_생성_요청(name, price).as(Product.class);
    }

    public static ExtractableResponse<Response> 상품목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }

    public static List<Product> 상품목록_조회() {
        return 상품목록_조회_요청().jsonPath().getList("$", Product.class);
    }

}
