package kitchenpos.product;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.dto.ProductRequest;

public class ProductAcceptanceAPI {

    public static ExtractableResponse<Response> 상품_생성_요청(String name, BigDecimal price) {
        ProductRequest productRequest = new ProductRequest(name, price);

        return AcceptanceTest.doPost("/api/products", productRequest);
    }

    public static ExtractableResponse<Response> 상품_조회_요청() {
        return AcceptanceTest.doGet("/api/products");
    }
}
