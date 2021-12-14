package kitchenpos.product;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

import static kitchenpos.AcceptanceTest.*;

public class ProductSteps {

    private static final String PRODUCT_URI = "/api/products";

    public static ProductResponse 상품_등록되어_있음(ProductRequest productRequest) {
        return 상품_등록_요청(productRequest).as(ProductResponse.class);
    }

    public static ExtractableResponse<Response> 상품_등록_요청(ProductRequest productRequest) {
        return post(PRODUCT_URI, productRequest);
    }

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return get(PRODUCT_URI);
    }
}
