package kitchenpos.rest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductCreateRequest;
import kitchenpos.resource.UriResource;
import org.springframework.http.MediaType;

public class ProductRestAssured {

    public static ExtractableResponse<Response> 상품_등록됨(Product product) {
        return 상품_등록_요청(new ProductCreateRequest(product.getName(), product.getPrice().value()));
    }

    public static ExtractableResponse<Response> 상품_등록_요청(ProductCreateRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
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
