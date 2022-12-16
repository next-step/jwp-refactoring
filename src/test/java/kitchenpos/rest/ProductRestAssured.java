package kitchenpos.rest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Product;
import kitchenpos.resource.UriResource;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

public class ProductRestAssured {

    public static ExtractableResponse<Response> 상품_등록됨(Product product) {
        return 상품_등록_요청(product.getName(), product.getPrice());
    }

    public static ExtractableResponse<Response> 상품_등록_요청(String name, BigDecimal price) {
        Product body = new Product(name, price);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post(UriResource.상품_API.uri())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(UriResource.상품_API.uri())
                .then().log().all()
                .extract();
    }
}
