package Acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("제품 생성 통합 테스트")
    @Test
    void createTest() {
        // given
        ProductRequest request = new ProductRequest("제품", BigDecimal.valueOf(1000L));

        // when
        ExtractableResponse<Response> response = 제품_생성_요청(request);

        // then
        ProductResponse actual = 제품_생성_성공(response);
        assertThat(actual).isNotNull()
                          .extracting(ProductResponse::getName)
                          .isEqualTo("제품");
    }

    @DisplayName("전체 제품 조회 통합 테스트")
    @Test
    void listTest() {
        // when
        ExtractableResponse<Response> response = 전체_제품_조회_요청();

        // then
        List<ProductResponse> actual = 전체_제품_조회_성공(response);
        assertThat(actual).isNotEmpty().hasSizeGreaterThanOrEqualTo(2);
    }

    public static ExtractableResponse<Response> 제품_생성_요청(final ProductRequest request) {
        // when
        return RestAssured.given().log().all()
                          .body(request)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().post("/api/products")
                          .then().log().all().extract();
    }

    public static ProductResponse 제품_생성_성공(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(ProductResponse.class);
    }

    public static ExtractableResponse<Response> 전체_제품_조회_요청() {
        // when
        return RestAssured.given().log().all()
                          .when().get("/api/products")
                          .then().log().all().extract();
    }

    public List<ProductResponse> 전체_제품_조회_성공(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.as(List.class);
    }
}
