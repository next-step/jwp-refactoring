package kitchenpos.product;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.utils.RestTestApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("상품 인수 테스트")
class ProductAcceptanceTest extends AcceptanceTest {

    private static final String URI = "/api/products";

    private ProductRequest 상품_강정치킨;
    private ProductRequest 상품_마늘치킨;

    @BeforeEach
    public void setUp() {
        super.setUp();

        상품_강정치킨 = new ProductRequest("강정치킨", BigDecimal.valueOf(17_000L));
        상품_마늘치킨 = new ProductRequest("마늘치킨", BigDecimal.valueOf(17_000L));
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void createProduct() {
        // when
        ExtractableResponse<Response> response = 상품_생성_요청(상품_강정치킨);

        // then
        상품_생성됨(response);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        // given
        ExtractableResponse<Response> 응답_강정치킨 = 상품_생성_요청(상품_강정치킨);
        ExtractableResponse<Response> 응답_마늘치킨 = 상품_생성_요청(상품_마늘치킨);

        // when
        ExtractableResponse<Response> 상품_목록_응답 = 상품_목록_요청();

        // then
        상품_목록_응답됨(상품_목록_응답);
        상품_목록_포함됨(상품_목록_응답, Arrays.asList(응답_강정치킨, 응답_마늘치킨));
    }

    private ExtractableResponse<Response> 상품_목록_요청() {
        return RestTestApi.get(URI);
    }

    private ExtractableResponse<Response> 상품_생성_요청(ProductRequest productRequest) {
        return RestTestApi.post(URI, productRequest);
    }

    private void 상품_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private void 상품_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 상품_목록_포함됨(ExtractableResponse<Response> response,
        List<ExtractableResponse<Response>> expectedResponses) {

        List<Long> responseIds = response.jsonPath().getList(".", ProductResponse.class).stream()
            .map(ProductResponse::getId)
            .collect(Collectors.toList());

        List<Long> expectedIds = expectedResponses.stream()
            .map(expectedResponse -> expectedResponse.as(ProductResponse.class))
            .map(ProductResponse::getId)
            .collect(Collectors.toList());

        assertThat(responseIds).containsAll(expectedIds);
    }
}
