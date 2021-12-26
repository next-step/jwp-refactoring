package kitchenpos.product.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Product;

@DisplayName("상품 관련 기능")
public class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품을 등록한다.")
    @Test
    void create() {
        ExtractableResponse 상품_등록_요청_응답 = 상품_등록_요청("제육볶음", 8900);

        상품_등록_요청됨(상품_등록_요청_응답);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        ExtractableResponse 상품_목록_요청_응답 = 상품_목록_요청();

        상품_목록_요청됨(상품_목록_요청_응답);
    }

    private ExtractableResponse 상품_목록_요청() {
        return RestAssured
            .given().log().all()
            .when().get("/api/products")
            .then().log().all().extract();
    }

    private ExtractableResponse 상품_등록_요청(String name, int price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(product)
            .when().post("/api/products")
            .then().log().all().extract();
    }

    private void 상품_등록_요청됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 상품_목록_요청됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
