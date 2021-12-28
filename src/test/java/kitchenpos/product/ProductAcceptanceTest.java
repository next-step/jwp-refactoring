package kitchenpos.product;

import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("상품 인수 테스트")
public class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품을 생성한다.")
    @Test
    void createProduct() {
        // given
        ProductRequest 후라이드치킨 = ProductRequest.of("후라이드치킨", 16000L);

        // when
        ExtractableResponse<Response> response = 상품_생성_요청(후라이드치킨);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("상품을 목록을 조회한다.")
    @Test
    void findAllProduct() {
        // when
        ExtractableResponse<Response> response = 상품_목록_조회됨();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static ExtractableResponse<Response> 상품_생성_요청(ProductRequest productRequest) {
        return ofRequest(Method.POST, "/api/products", productRequest);
    }

    private ExtractableResponse<Response> 상품_목록_조회됨() {
        return ofRequest(Method.GET, "/api/products");
    }

    public static ProductResponse 상품_생성됨(ProductRequest productRequest) {
        return 상품_생성_요청(productRequest)
                .as(ProductResponse.class);
    }
}
