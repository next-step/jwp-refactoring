package kitchenpos.product.generator;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.product.acceptance.RestAssuredRequest;
import kitchenpos.product.dto.ProductCreateRequest;

import java.math.BigDecimal;
import java.util.Collections;

public class ProductGenerator {
    private static final String PATH = "/api/products";

    private ProductGenerator() {}

    public static ProductCreateRequest 상품_생성_요청(String name, int price) {
        return new ProductCreateRequest(name, new BigDecimal(price));
    }

    public static ExtractableResponse<Response> 상품_생성_API_요청(String name, int price) {
        return RestAssuredRequest.postRequest(PATH, Collections.emptyMap(), 상품_생성_요청(name, price));
    }

    public static ExtractableResponse<Response> 상품_목록_API_요청() {
        return RestAssuredRequest.getRequest(PATH, Collections.emptyMap());
    }

    public static ExtractableResponse<Response> 상품_조회_API_요청(Long id) {
        return RestAssuredRequest.getRequest(PATH + "/{id}", Collections.emptyMap(), id);
    }
}
