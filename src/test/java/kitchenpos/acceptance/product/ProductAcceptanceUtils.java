package kitchenpos.acceptance.product;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class ProductAcceptanceUtils {
    private ProductAcceptanceUtils() {}

    public static ExtractableResponse<Response> 상품_등록_요청(String name, long price) {
        Map<String, String> body = new HashMap<>();
        body.put("name", name);
        body.put("price", Long.toString(price));

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().post("/api/products")
            .then().log().all()
            .extract();
    }

    public static void 상품_등록_완료(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .when().get("/api/products")
            .then().log().all()
            .extract();
    }

    public static void 상품_목록_조회_완료(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ProductId 상품_ID_추출(ExtractableResponse<Response> response) {
        return new ProductId(response.jsonPath().get("id"));
    }

    public static List<String> 상품_목록_이름_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("name", String.class);
    }

    public static List<Long> 상품_목록_가격_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("price", BigDecimal.class).stream()
            .map(BigDecimal::longValue)
            .collect(Collectors.toList());
    }
}
