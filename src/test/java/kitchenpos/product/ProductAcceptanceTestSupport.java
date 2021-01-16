package kitchenpos.product;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.HttpStatusAssertion;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class ProductAcceptanceTestSupport extends AcceptanceTest {
    public static ExtractableResponse<Response> 상품_생성_요청(Map<String, Object> params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 모든_상품_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 상품_등록되어_있음(String name, Object price) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("price", price);
        ExtractableResponse<Response> createResponse = 상품_생성_요청(params);
        상품_생성_완료(createResponse);
        return createResponse;
    }

    public static void 상품_생성_완료(ExtractableResponse<Response> response) {
        HttpStatusAssertion.CREATED(response);
    }

    public static void 상품_목록_응답(ExtractableResponse<Response> response) {
        HttpStatusAssertion.OK(response);
    }
}
