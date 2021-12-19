package kitchenpos.menu.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;

@DisplayName("상품 관련 기능")
public class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품을 등록한다.")
    @Test
    void createProduct() {
        // given
        ProductRequest request = new ProductRequest("강정치킨", new BigDecimal(17000));

        // when
        ExtractableResponse<Response> response = 상품_생성_요청(request);

        // then
        상품_생성됨(response);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void findAllProduct() {
        // given
        List<ProductResponse> createdResponses = Arrays.asList(
            상품_생성_요청(new ProductRequest("강정치킨", new BigDecimal(17000)))
                .as(ProductResponse.class),
            상품_생성_요청(new ProductRequest("후라이드", new BigDecimal(16000)))
                .as(ProductResponse.class)
        );

        // when
        ExtractableResponse<Response> response = 상품_목록_조회_요청();

        // then
        상품_목록_조회됨(response, createdResponses);

    }

    public static ExtractableResponse<Response> 상품_생성_요청(ProductRequest request) {
        return RestAssured
            .given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/api/products")
            .then().log().all().extract();
    }

    private ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/api/products")
            .then().log().all().extract();
    }

    static void 상품_생성됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 상품_목록_조회됨(ExtractableResponse<Response> response,
        List<ProductResponse> responses) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(new TypeRef<List<ProductResponse>>() {
        }))
            .containsExactlyElementsOf(responses);
    }
}
