package kitchenpos.menu.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceNewTest;
import kitchenpos.menu.dto.ProductRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

@DisplayName("상품 관련 기능")
class ProductAcceptanceTest extends AcceptanceNewTest {

    @DisplayName("상품 등록")
    @Test
    void create() {
        // given
        ProductRequest request = new ProductRequest("후라이드치킨", BigDecimal.valueOf(10000));

        // when
        ExtractableResponse<Response> response = 상품_등록_요청(request);

        // then
        상품_둥록됨(response);
    }

    @DisplayName("상품 목록")
    @Test
    void list() {
        ProductRequest request = new ProductRequest("후라이드치킨", BigDecimal.valueOf(10000));
        상품_등록_요청(request);
        ProductRequest request2 = new ProductRequest("양념치킨", BigDecimal.valueOf(12000));
        상품_등록_요청(request2);

        // when
        ExtractableResponse<Response> response = 상품_목록_조회_요청();

        // then
        상품_목록_조회됨(response, HttpStatus.OK);
    }

    private ExtractableResponse<Response> 상품_목록_조회_요청() {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/api/products")
                .then().log().all().extract();
        return response;
    }

    private void 상품_목록_조회됨(ExtractableResponse<Response> response, HttpStatus ok) {
        Assertions.assertThat(response.statusCode()).isEqualTo(ok.value());
    }


    private ExtractableResponse<Response> 상품_등록_요청(ProductRequest params) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/products")
                .then().log().all().extract();
    }

    private void 상품_둥록됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

}