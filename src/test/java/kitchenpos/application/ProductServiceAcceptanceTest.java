package kitchenpos.application;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


class ProductServiceAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품을 등록한다")
    @Test
    void createTest() {

        ProductRequest productRequest = new ProductRequest("후라이드치킨", BigDecimal.valueOf(18000L));

        ExtractableResponse<Response> createResponse = ProductFactory.상품_생성_요청(productRequest);
        ProductResponse createdProduct = 상품이_생성됨(createResponse);
    }

    @DisplayName("상품을 조회한다")
    @Test
    void getListTest() {

        ProductRequest productRequest = new ProductRequest("후라이드치킨", BigDecimal.valueOf(18000L));

        ExtractableResponse<Response> createResponse = ProductFactory.상품_생성_요청(productRequest);
        ProductResponse createdProduct = 상품이_생성됨(createResponse);

        ExtractableResponse<Response> getResponse = ProductFactory.상품_조회_요청();
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ProductResponse 상품이_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(ProductResponse.class);
    }
}
