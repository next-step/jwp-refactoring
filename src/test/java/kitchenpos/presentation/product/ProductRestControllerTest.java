package kitchenpos.presentation.product;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.dto.product.ProductDto;
import kitchenpos.testassistance.config.TestConfig;

@DisplayName("상품 API기능에 관한")
public class ProductRestControllerTest extends TestConfig {
    @DisplayName("상품이 저장된다.")
    @Test
    void save_product() {
        // given
        ProductDto product = ProductDto.of("강정치킨", BigDecimal.valueOf(17_000));

        // when
        ExtractableResponse<Response> response = 상품_저장요청(product);

        // then
        상품_저장됨(response);
    }

    @DisplayName("상품이 조회된다.")
    @Test
    void search_product() {
        // when
        ExtractableResponse<Response> response = 상품_조회요청();

        // given
        상품_조회됨(response);
    }

    private void 상품_저장됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 상품_저장요청(ProductDto product) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    private void 상품_조회됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 상품_조회요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }
}
