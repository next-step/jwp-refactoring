package kitchenpos.acceptance;

import static kitchenpos.acceptance.ProductRestAssured.상품_등록되어_있음;
import static kitchenpos.acceptance.ProductRestAssured.상품_목록_조회_요청;
import static kitchenpos.acceptance.ProductRestAssured.상품_생성_요청;
import static kitchenpos.domain.ProductTestFixture.generateProduct;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("상품 관련 인수 테스트")
public class ProductAcceptanceTest extends AcceptanceTest {

    private Product 감자튀김;
    private Product 콜라;

    @BeforeEach
    public void setUp() {
        super.setUp();
        감자튀김 = generateProduct(1L, "감자튀김", BigDecimal.valueOf(3000L));
        콜라 = generateProduct(2L, "콜라", BigDecimal.valueOf(1500L));
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void createProduct() {
        // when
        ExtractableResponse<Response> response = 상품_생성_요청(감자튀김);

        // then
        상품_생성됨(response);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void findAllProducts() {
        // given
        ExtractableResponse<Response> createResponse1 = 상품_등록되어_있음(감자튀김);
        ExtractableResponse<Response> createResponse2 = 상품_등록되어_있음(콜라);

        // when
        ExtractableResponse<Response> response = 상품_목록_조회_요청();

        // then
        상품_목록_응답됨(response);
        상품_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }

    private static void 상품_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private static void 상품_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 상품_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedProductIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> resultProductIds = response.jsonPath().getList(".", Product.class).stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        assertThat(resultProductIds).containsAll(expectedProductIds);
    }
}
