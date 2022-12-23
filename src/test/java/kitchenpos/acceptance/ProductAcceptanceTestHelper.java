package kitchenpos.acceptance;


import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import kitchenpos.domain.Product;

class ProductAcceptanceTestHelper {

    static ExtractableResponse<Response> createProduct(String name, BigDecimal price) {
        final Product requestBody = new Product(name, price);
        return AcceptanceTestHelper.post("/api/products", requestBody);
    }

    static ExtractableResponse<Response> getProducts() {
        return AcceptanceTestHelper.get("/api/products");
    }
}
