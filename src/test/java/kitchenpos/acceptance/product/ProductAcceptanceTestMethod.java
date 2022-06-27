package kitchenpos.acceptance.product;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.product.ProductRequest;
import kitchenpos.dto.product.ProductResponse;
import org.springframework.http.HttpStatus;

public class ProductAcceptanceTestMethod extends AcceptanceTest {

    private static final String PRODUCT_PATH = "/api/products";
    private static final String DOT = ".";

    public static ExtractableResponse<Response> 상품_등록_요청(ProductRequest params) {
        return post(PRODUCT_PATH, params);
    }

    public static ExtractableResponse<Response> 상품_등록되어_있음(ProductRequest params) {
        return 상품_등록_요청(params);
    }

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return get(PRODUCT_PATH);
    }

    public static void 상품_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(parseURIFromLocationHeader(response)).isNotBlank();
    }

    public static void 상품_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 상품_목록_포함됨(ExtractableResponse<Response> response,
                                    List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedIds = createdResponses.stream()
                .map(AcceptanceTest::parseIdFromLocationHeader)
                .collect(Collectors.toList());

        List<Long> actualIds = response.jsonPath().getList(DOT, ProductResponse.class)
                .stream()
                .map(ProductResponse::getId)
                .collect(Collectors.toList());

        assertThat(actualIds).containsAll(expectedIds);
    }
}