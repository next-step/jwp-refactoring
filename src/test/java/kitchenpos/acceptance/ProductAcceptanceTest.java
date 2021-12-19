package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("상품 관련 기능")
class ProductAcceptanceTest extends AcceptanceTest {

    private static final String API_URL = "/api/products";

    @DisplayName("상품을 관리한다.")
    @Test
    void manageMenuGroup() {
        // given
        Product product = new Product();
        product.setName("강정치킨");
        product.setPrice(BigDecimal.valueOf(17_000));

        // when
        ExtractableResponse<Response> 상품_생성_응답 = 상품_생성_요청(product);
        // then
        상품_생성됨(상품_생성_응답);

        // when
        ExtractableResponse<Response> 상품_목록_조회_응답 = 상품_목록_조회_요청();
        // then
        상품_목록_조회됨(상품_목록_조회_응답);
    }

    private static ExtractableResponse<Response> 상품_생성_요청(Product params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(API_URL)
                .then().log().all()
                .extract();
    }

    private void 상품_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(API_URL)
                .then().log().all()
                .extract();
    }

    private void 상품_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static Product 상품_등록되어_있음(String name, long price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));

        ExtractableResponse<Response> response = 상품_생성_요청(product);
        return response.as(Product.class);
    }
}
