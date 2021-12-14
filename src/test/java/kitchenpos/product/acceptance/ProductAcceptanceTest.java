package kitchenpos.product.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static kitchenpos.product.ProductSteps.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("상품 인수 테스트")
public class ProductAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("상품을 관리한다.")
    void manageProduct() {
        // given
        ProductRequest productRequest = new ProductRequest("후라이드치킨", new BigDecimal(16_000));

        // when
        ExtractableResponse<Response> createResponse = 상품_등록_요청(productRequest);

        // then
        상품_등록됨(createResponse);

        // when
        ExtractableResponse<Response> listResponse = 상품_목록_조회_요청();

        // then
        상품_목록_조회됨(listResponse);
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
