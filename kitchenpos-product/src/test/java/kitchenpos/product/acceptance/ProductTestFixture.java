package kitchenpos.product.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.TestFixture;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

public class ProductTestFixture extends TestFixture {

    public static final String PRODUCT_BASE_URI = "/api/products";

    public static ExtractableResponse<Response> 상품_생성_요청함(String name, BigDecimal price) {
        ProductRequest productRequest = new ProductRequest(name, price);
        return post(PRODUCT_BASE_URI, productRequest);
    }

    public static ExtractableResponse<Response> 상품_목록_조회_요청함() {
        return get(PRODUCT_BASE_URI);
    }

    public static void 상품_조회_요청_응답됨(ExtractableResponse<Response> response) {
        ok(response);
    }

    public static void 상품_생성됨(ExtractableResponse<Response> response) {
        ProductResponse productResponse = response.as(ProductResponse.class);
        created(response);
        assertAll(
                ()-> assertThat(productResponse.getId()).isNotNull(),
                ()-> assertThat(productResponse.getName()).isNotNull(),
                ()-> assertThat(productResponse.getPrice()).isNotNull()
        );

    }

    public static void 상품_조회_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> productResponses) {

        List<Long> expectIds = productResponses.stream()
                .map(r -> r.as(ProductResponse.class))
                .map(ProductResponse::getId)
                .collect(Collectors.toList());

        List<Long> actualIds = response.jsonPath()
                .getList(".", ProductResponse.class)
                .stream()
                .map(ProductResponse::getId)
                .collect(Collectors.toList());


        assertThat(actualIds).containsAll(expectIds);
    }

}
