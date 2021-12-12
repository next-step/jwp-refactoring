package kitchenpos.product.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@DisplayName("상품 인수 테스트")
public class ProductAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("상품을 등록한다.")
    void create() {
        // given
        ProductRequest productRequest = new ProductRequest("매운양념치킨", new BigDecimal(18_000));

        // when
        ExtractableResponse<Response> response = 상품_등록_요청(productRequest);

        // then
        상품_등록됨(response);
    }

    @Test
    @DisplayName("상품의 목록을 조회한다.")
    void list() {
        // when
        ExtractableResponse<Response> response = 상품_목록_조회_요청();

        // then
        상품_목록_조회됨(response);
    }

    public static ExtractableResponse<Response> 상품_등록_요청(ProductRequest productRequest) {
        return post("/api/products", productRequest);
    }

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return get("/api/products");
    }

    private void 상품_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private void 상품_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".", Product.class).size()).isPositive();
    }
}
