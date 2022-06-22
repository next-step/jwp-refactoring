package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("상품 관련 기능")
public class ProductAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("상품을 등록한다.")
    void createProduct() {
        // when
        ExtractableResponse<Response> response = 상품_등록_요청("강정치킨", new BigDecimal(17000));

        // then
        상품_등록됨(response);
    }

    @Test
    @DisplayName("상품 가격이 0미만이면 등록 실패한다.")
    void createProductOfUnderZeroPrice() {
        // when
        ExtractableResponse<Response> response = 상품_등록_요청("강정치킨", new BigDecimal(-1000));

        // then
        상품_등록_실패됨(response);
    }

    @Test
    @DisplayName("상품을 조회한다.")
    void getProducts() {
        // when
        ExtractableResponse<Response> response = 상품_목록_조회_요청();

        // then
        상품_목록_조회됨(response);
    }

    public static ExtractableResponse<Response> 상품_등록_요청(String name, BigDecimal price) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("price", price.toString());

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/products")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/products")
                .then().log().all().extract();
    }


    public static void 상품_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 상품_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 상품_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
