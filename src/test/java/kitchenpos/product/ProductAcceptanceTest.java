package kitchenpos.product;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class ProductAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    @DisplayName("상품을 생성한다")
    void 상품을_생성한다() {
        // when
        ExtractableResponse<Response> response = 상품_생성_요청("양념치킨", new BigDecimal(18000));

        // then
        상품_생성됨(response);
    }

    @Test
    @DisplayName("상품을 조회한다")
    void 상품을_조회한다() {
        // given
        상품_생성_요청("양념치킨", new BigDecimal(18000));

        // when
        ExtractableResponse<Response> response = 상품_조회_요청();

        // then
        assertThat(response.jsonPath().getList("id")).hasSize(1);
    }

    public static ExtractableResponse<Response> 상품_생성되어_있음(String name, BigDecimal price) {
        return 상품_생성_요청(name, price);
    }

    public static ExtractableResponse<Response> 상품_생성_요청(String name, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);

        return AcceptanceTest.doPost("/api/products", product);
    }

    public static ExtractableResponse<Response> 상품_조회_요청() {
        return AcceptanceTest.doGet("/api/products");
    }

    public static void 상품_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }
}
