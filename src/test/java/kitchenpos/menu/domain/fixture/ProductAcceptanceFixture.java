package kitchenpos.menu.domain.fixture;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.domain.product.Product;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.ProductRequest;

import java.math.BigDecimal;

import static kitchenpos.utils.AcceptanceFixture.get;
import static kitchenpos.utils.AcceptanceFixture.post;

public class ProductAcceptanceFixture {

    public static ExtractableResponse<Response> 상품_생성_요청(ProductRequest productRequest) {
        return post("/api/products", productRequest);
    }

    public static ExtractableResponse<Response> 상품_조회_요청() {
        return get("/api/products");
    }

}
