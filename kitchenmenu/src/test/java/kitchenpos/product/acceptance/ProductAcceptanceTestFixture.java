package kitchenpos.product.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ProductAcceptanceTestFixture extends AcceptanceTest {

    public ProductRequest 떡볶이;
    public ProductRequest 튀김;
    public ProductRequest 순대;

    @BeforeEach
    public void setUp() {
        super.setUp();

        떡볶이 = new ProductRequest("떡볶이", BigDecimal.valueOf(4500));
        튀김 = new ProductRequest("튀김", BigDecimal.valueOf(2500));
        순대 = new ProductRequest("순대", BigDecimal.valueOf(4000));
    }

    public static ExtractableResponse<Response> 상품_생성_요청(ProductRequest productRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    public static ProductResponse 상품_생성_되어있음(ProductRequest productRequest) {
        return 상품(상품_생성_요청(productRequest));
    }

    public static ProductResponse 상품(ExtractableResponse<Response> response) {
        return response.jsonPath().getObject("", ProductResponse.class);
    }

    public static void 상품_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 상품_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }

    public static List<ProductResponse> 상품_목록(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("", ProductResponse.class);
    }

    public static void 상품_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
