package kitchenpos.product.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kitchenpos.acceptance.BaseAcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ProductAcceptanceTest extends BaseAcceptanceTest {
    @DisplayName("상품을 관리한다")
    @Test
    void manageProduct() {
        // given
        final String productName1 = "삼겹살";
        final String productName2 = "김치찌개";

        // when
        ExtractableResponse<Response> created1 = 상품_생성_요청(productName1, BigDecimal.valueOf(14000L));
        // then
        상품_생성됨(created1);

        // when
        ExtractableResponse<Response> created2 = 상품_생성_요청(productName2, BigDecimal.valueOf(8000L));
        // then
        상품_생성됨(created2);


        // when
        ExtractableResponse<Response> list = 상품_목록_조회_요청();
        // then
        상품_목록_조회됨(list);
        // then
        상품_목록_포함됨(list, Arrays.asList(productName1, productName2));
    }

    public static ExtractableResponse<Response> 상품_생성_요청(final String name, final BigDecimal price) {
        final Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("price", price);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }

    public static void 상품_생성됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 상품_목록_조회됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 상품_목록_포함됨(final ExtractableResponse<Response> response, final List<String> productNames) {
        assertThat(response.body().asString()).contains(productNames);
    }
}
