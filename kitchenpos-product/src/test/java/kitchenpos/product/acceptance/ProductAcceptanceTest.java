package kitchenpos.product.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import kitchenpos.product.AcceptanceTest;
import kitchenpos.product.dto.ProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("상품을 관리한다.")
public class ProductAcceptanceTest extends AcceptanceTest {
    @Test
    @DisplayName("상품을 생성한다.")
    void createMenuGroup() {
        // when
        ExtractableResponse<Response> response = 상품_생성_요청("강정치킨", 17_000L);

        // then
        상품_생성됨(response);
    }

    @Test
    @DisplayName("상품 목록을 조회한다.")
    void findAll() {
        // when
        ExtractableResponse<Response> response = 상품_목록_조회_요청();

        // then
        상품_목록_응답됨(response);
    }

    public static ExtractableResponse<Response> 상품_생성_요청(String name, Long price) {
        ProductRequest productRequest = new ProductRequest(name, new BigDecimal(price));

        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(productRequest)
            .when().post("/api/products")
            .then().log().all().extract();
    }

    private static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/products")
            .then().log().all().extract();
    }

    public static void 상품_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 상품_목록_응답됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
