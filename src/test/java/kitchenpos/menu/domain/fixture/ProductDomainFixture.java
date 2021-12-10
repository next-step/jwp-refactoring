package kitchenpos.menu.domain.fixture;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.domain.product.Product;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;

import java.math.BigDecimal;

import static kitchenpos.utils.AcceptanceFixture.get;
import static kitchenpos.utils.AcceptanceFixture.post;

public class ProductDomainFixture {
    public static Product 후라이드 = product("후라이드", BigDecimal.valueOf(15000));
    public static ProductRequest 후라이드_요청 = ProductRequest.of("후라이드", BigDecimal.valueOf(15000));
    public static ProductResponse 후라이드_등록됨 = 상품_생성_요청(후라이드_요청).as(ProductResponse.class);
    public static Product 사이다 = product("사이다", BigDecimal.valueOf(1000));
    public static ProductRequest 사이다_요청 = ProductRequest.of("사이다", BigDecimal.valueOf(1000));
    public static ProductResponse 사이다_등록됨 = 상품_생성_요청(사이다_요청).as(ProductResponse.class);

    public static Product 양념소스 = product("양념 소스", BigDecimal.valueOf(500));
    public static ProductRequest 양념소스_요청 = ProductRequest.of("양념소스", BigDecimal.valueOf(1000));
    public static ProductResponse 양념소스_등록됨 = 상품_생성_요청(양념소스_요청).as(ProductResponse.class);
    public static Product product(String name, BigDecimal price) {
        return new Product(name, price);
    }

    public static ExtractableResponse<Response> 상품_생성_요청(ProductRequest productRequest) {
        return post("/api/products", productRequest);
    }

    public static ExtractableResponse<Response> 상품_조회_요청() {
        return get("/api/products");
    }
}
