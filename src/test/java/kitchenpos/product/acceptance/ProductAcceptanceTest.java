package kitchenpos.product.acceptance;

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

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("제품 관리")
public class ProductAcceptanceTest extends AcceptanceTest {
    @DisplayName("제품을 관리한다")
    @Test
    void manage() {
        제품_생성();
        제품_조회();
    }

    private void 제품_생성() {
        ProductRequest request = createRequest();
        ExtractableResponse<Response> createdResponse = 생성_요청(request);

        생성됨(createdResponse, request);
    }

    private void 제품_조회() {
        ExtractableResponse<Response> selectedResponse = 조회_요청();

        조회됨(selectedResponse);
    }

    public static ProductRequest createRequest() {
        return new ProductRequest("강정치킨", 17_000);
    }

    public static ExtractableResponse<Response> 생성_요청(ProductRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    public static void 생성됨(ExtractableResponse<Response> response, ProductRequest request) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        ProductResponse product = response.as(ProductResponse.class);
        assertThat(product.getName()).isEqualTo(request.getName());
        assertThat(product.getPrice()).isEqualTo(request.getPrice());
    }

    public static ExtractableResponse<Response> 조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }

    public static void 조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<ProductResponse> menuGroups = Arrays.asList(response.as(ProductResponse[].class));
        assertThat(menuGroups.size()).isEqualTo(1);
    }
}
