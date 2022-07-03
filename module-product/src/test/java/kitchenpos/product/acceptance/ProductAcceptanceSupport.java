package kitchenpos.product.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.product.dto.request.ProductRequest;
import kitchenpos.product.dto.response.ProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductAcceptanceSupport {

    public static ExtractableResponse<Response> 상품_등록요청(ProductRequest productRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    public static void 상품_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static ExtractableResponse<Response> 상품목록_조회요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/products")
                .then().log().all().
                extract();
    }

    public static void 상품목록_조회됨(ExtractableResponse<Response> response, int size) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<ProductResponse> result = response.jsonPath().getList(".", ProductResponse.class);
        assertThat(result).isNotNull();
        assertThat(result).hasSize(size);
    }
}
