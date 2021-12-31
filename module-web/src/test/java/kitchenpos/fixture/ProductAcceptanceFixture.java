package kitchenpos.fixture;


import static kitchenpos.common.AcceptanceFixture.*;

import java.math.*;

import io.restassured.response.*;
import kitchenpos.common.*;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.*;

public class ProductAcceptanceFixture extends AcceptanceTest {
    public static Product 후라이드치킨 = product("후라이드치킨", BigDecimal.valueOf(13000));
    public static ProductRequest 후라이드치킨_요청 = ProductRequest.of(후라이드치킨.getName(), 후라이드치킨.getPrice());

    public static Product 양념치킨 = product("양념치킨", BigDecimal.valueOf(15000));
    public static ProductRequest 양념치킨_요청 = ProductRequest.of(양념치킨.getName(), 양념치킨.getPrice());

    public static ProductResponse 후라이드치킨_생성됨() {
        return 상품_생성_요청(후라이드치킨_요청).as(ProductResponse.class);
    }
    public static ProductResponse 양념치킨_생성됨() {
        return 상품_생성_요청(양념치킨_요청).as(ProductResponse.class);
    }

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
