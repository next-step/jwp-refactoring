package kitchenpos.product.ui;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.product.domain.Product;

public class ProductAcceptanceTestHelper {
    private ProductAcceptanceTestHelper() {
    }

    public static ExtractableResponse<Response> 상품_생성_요청(Map<String, String> params) {
        return RestAssured
            .given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/api/products")
            .then().log().all().extract();
    }

    public static void 상품_생성됨(ExtractableResponse<Response> createResponse) {
        Assertions.assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createResponse.header("Location")).isNotBlank();
    }

    public static void 상품_생성_실패됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static ExtractableResponse<Response> 상품_조회_요청() {
        return RestAssured
            .given().log().all()
            .when().get("/api/products")
            .then().log().all().extract();
    }

    public static void 상품_조회됨(ExtractableResponse<Response> getResponse) {
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 상품갯수_예상과_일치(ExtractableResponse<Response> getResponse, int expectedLength) {
        List<Product> actual = getResponse.jsonPath().getList(".");
        assertThat(actual).hasSize(expectedLength);
    }
}
