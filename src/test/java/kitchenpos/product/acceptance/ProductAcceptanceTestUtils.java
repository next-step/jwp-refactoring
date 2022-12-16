package kitchenpos.product.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ProductAcceptanceTestUtils {
    private static final String PRODUCT_PATH = "/api/products";

    private ProductAcceptanceTestUtils() {}

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get(PRODUCT_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 상품_생성_요청(String name, BigDecimal price) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ProductRequest(name, price))
                .when().post(PRODUCT_PATH)
                .then().log().all()
                .extract();
    }

    public static ProductResponse 상품_등록되어_있음(String name, BigDecimal price) {
        ExtractableResponse<Response> response = 상품_생성_요청(name, price);
        상품_생성됨(response);
        return response.as(ProductResponse.class);
    }

    public static void 상품_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 상품_생성_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 상품_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 상품_목록_포함됨(ExtractableResponse<Response> response, String... productNames) {
        List<String> actual = response.jsonPath()
                .getList("name", String.class);
        assertThat(actual).containsExactly(productNames);
    }
}
