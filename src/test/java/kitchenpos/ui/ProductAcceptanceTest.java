package kitchenpos.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

@DisplayName("상품 관련 기능")
class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품 등록")
    @Test
    void create() {
        // given
        Map<String, Object> params = new HashMap<>();
        params.put("name", "후라이드치킨");
        params.put("price", 10000);

        // when
        ExtractableResponse<Response> response = 상품_등록_요청(params);

        // then
        상품_둥록됨(response);
    }

    private ExtractableResponse<Response> 상품_등록_요청(Map<String, Object> params) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/products")
                .then().log().all().extract();
    }

    private void 상품_둥록됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

}