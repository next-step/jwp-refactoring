package kitchenpos.product;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import org.springframework.http.MediaType;

public class ProductFixture {

    public static final Product 강정치킨 = new Product(1L, "강정치킨", new BigDecimal(17_000));
    public static final Product 개손해치킨 = new Product(2L, "개손해치킨", new BigDecimal(1));

    public static ExtractableResponse<Response> 상품_등록(String name, BigDecimal price) {
        ProductRequest productRequest = new ProductRequest(null, name, price);

        return RestAssured
            .given().log().all()
            .body(productRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/api/products")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 상품_목록_조회() {

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/products")
            .then().log().all()
            .extract();
    }

    public static ProductRequest createProductRequest(Product product) {
        return new ProductRequest(product.getId(), product.getName(), product.getPrice());
    }
}
