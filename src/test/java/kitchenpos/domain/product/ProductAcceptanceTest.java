package kitchenpos.domain.product;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("상품 관련 기능")
public class ProductAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("상품을 등록할 수 있다.")
    void createProduct() {
        // given
        Product 후라이드 = new Product("후라이드", BigDecimal.valueOf(17000));

        // when
        ExtractableResponse<Response> 상품_등록_요청_응답 = 상품_등록을_요청(후라이드);

        // then
        상품_등록됨(상품_등록_요청_응답);

    }

    private void 상품_등록됨(ExtractableResponse<Response> 상품_등록_요청_응답) {
        Product 등록된_상품 = 상품_등록_요청_응답.as(Product.class);
        assertThat(등록된_상품.getId()).isNotNull();
        assertThat(등록된_상품.getName()).isEqualTo("후라이드");
        assertThat(등록된_상품.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(17000));
    }

    public ExtractableResponse<Response> 상품_등록을_요청(Product product) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }
}
