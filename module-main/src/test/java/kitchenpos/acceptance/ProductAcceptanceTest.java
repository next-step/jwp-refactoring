package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.AcceptanceTest;
import kitchenpos.fixture.ProductFactory;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("상품 관련 기능")
public class ProductAcceptanceTest extends AcceptanceTest {

    /**
     * Feature: 상품을 관리한다.
     * <p>
     * Scenario: 상품 관리
     * <p>
     * When 상품 등록 요청
     * <p>
     * Then 상품 등록됨
     * <p>
     * When 상품 목록 조회 요청
     * <p>
     * Then 상품 목록이 조회됨
     */
    @DisplayName("상품을 관리")
    @Test
    void 상품_관리() {
        ExtractableResponse<Response> response;
        // when 상품 등록 요청
        ExtractableResponse<Response> createdResponse = 상품_등록_요청("상품", 1000);
        // then 상품 등록됨
        상품_등록됨(createdResponse);

        // when 상품 목록 조회 요청
        response = 상품_목록_조회();
        // then 상품 목록이 조회됨
        상품_목록_조회됨(response);
        // then 상품 목록이 조회됨
        상품_목록_포함됨(response, Arrays.asList(createdResponse.as(ProductResponse.class)));
    }

    public static ExtractableResponse<Response> 상품_등록_요청(String name, double price) {
        ProductRequest request = ProductFactory.createProductRequest(name, BigDecimal.valueOf(price));

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 상품_등록_요청(Product product) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 상품_목록_조회() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }


    public static void 상품_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 상품_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 상품_목록_포함됨(ExtractableResponse<Response> response, List<ProductResponse> expectedProducts) {
        List<Long> resultProductIds = response.jsonPath().getList(".", ProductResponse.class).stream()
                .map(ProductResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedProductIds = expectedProducts.stream()
                .map(ProductResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultProductIds).containsAll(expectedProductIds);
    }
}
