package kitchenpos.application;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


class ProductServiceAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품을 등록한다")
    @Test
    void createTest() {

        Product product = new Product();
        product.setName("후라이드치킨");
        product.setPrice(BigDecimal.valueOf(18000));

        ExtractableResponse<Response> createResponse = ProductFactory.상품_생성_요청(product);
        Product createdProduct = 상품이_생성됨(createResponse);
    }

    @DisplayName("상품을 조회한다")
    @Test
    void getListTest() {

        Product product = new Product();
        product.setName("후라이드치킨");
        product.setPrice(BigDecimal.valueOf(18000));

        ExtractableResponse<Response> createResponse = ProductFactory.상품_생성_요청(product);
        Product createdProduct = 상품이_생성됨(createResponse);

        ExtractableResponse<Response> getResponse = ProductFactory.상품_조회_요청();
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static Product 상품이_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(Product.class);
    }
}
