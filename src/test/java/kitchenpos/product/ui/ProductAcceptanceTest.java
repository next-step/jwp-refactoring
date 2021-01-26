package kitchenpos.product.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.utils.TestHelper.등록되어_있지_않은_product_id;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("상품 관련 기능")
class ProductAcceptanceTest extends AcceptanceTest {
    @Test
    void createProduct() {
        ProductRequest productRequest = new ProductRequest(등록되어_있지_않은_product_id, "스노윙치킨", BigDecimal.valueOf(18000));

        ExtractableResponse<Response> response = 상품_생성_요청(productRequest);

        상품_생성됨(response);
    }

    @Test
    void getProducts() {
        ExtractableResponse<Response> response = 상품_목록_조회_요청();

        상품_목록_조회됨(response);
        상품_목록_포함됨(response, Arrays.asList("후라이드", "양념치킨", "반반치킨", "통구이", "간장치킨", "순살치킨"));
    }

    private static ExtractableResponse<Response> 상품_생성_요청(ProductRequest productRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }

    private static void 상품_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private static void 상품_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 상품_목록_포함됨(ExtractableResponse<Response> response, List<String> resultNames) {
        List<String> menuGroupNames = response.jsonPath().getList(".", ProductResponse.class).stream()
                .map(ProductResponse::getName)
                .collect(Collectors.toList());

        assertThat(menuGroupNames).containsAll(resultNames);
    }
}
