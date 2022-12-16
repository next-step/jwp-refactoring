package kitchenpos.Acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import kitchenpos.TestFixture;
import org.assertj.core.api.Assertions;

public class ProductTestFixture extends TestFixture {

    public static final String PRODUCT_BASE_URI = "/api/products";

    public static ExtractableResponse<Response> 상품_생성_요청함(Product product) {
        return post(PRODUCT_BASE_URI, product);
    }

    public static ExtractableResponse<Response> 상품_목록_조회_요청함() {
        return get(PRODUCT_BASE_URI);
    }

    public static void 상품_조회_요청_응답됨(ExtractableResponse<Response> response) {
        ok(response);
    }

    public static void 상품_생성됨(ExtractableResponse<Response> response) {
        created(response);
    }

    public static void 상품_조회_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> productResponses) {
        List<Long> actualIds = response.jsonPath()
                .getList(".", Product.class)
                .stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        List<Long> expectIds = productResponses.stream()
                .map(r -> r.as(Product.class))
                .collect(Collectors.toList())
                .stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        Assertions.assertThat(actualIds).containsAll(expectIds);
    }

}
