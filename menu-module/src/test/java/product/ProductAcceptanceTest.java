package product;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("상품 관련 기능")
public class ProductAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("상품을 등록할 수 있다.")
    void createProduct() {
        // given
        ProductCreateRequest 후라이드 = new ProductCreateRequest("후라이드", BigDecimal.valueOf(17000));

        // when
        ExtractableResponse<Response> 상품_등록_요청_응답 = 상품_등록을_요청(후라이드);

        // then
        상품_등록됨(상품_등록_요청_응답);

    }

    private void 상품_등록됨(ExtractableResponse<Response> response) {
        Product 등록된_상품 = response.as(Product.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(등록된_상품.getId()).isNotNull(),
                () -> assertThat(등록된_상품.getName()).isEqualTo("후라이드"),
                () -> assertThat(등록된_상품.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(17000))
        );
    }

    public ExtractableResponse<Response> 상품_등록을_요청(ProductCreateRequest request) {
        return post("/api/products", request);
    }
}
