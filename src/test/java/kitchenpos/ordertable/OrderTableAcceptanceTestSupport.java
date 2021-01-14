package kitchenpos.ordertable;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.HttpStatusAssertion;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class OrderTableAcceptanceTestSupport extends AcceptanceTest {
    public static ExtractableResponse<Response> 주문_테이블_생성_요청(Map<String, Object> params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_주문_상태_변경_요청(ExtractableResponse<Response> createResponse, Map<String, Object> params) {
        String location = createResponse.header("Location");
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put(location + "/empty")
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> 주문_테이블_방문한_손님_수_변경_요청(ExtractableResponse<Response> createResponse, Map<String, Object> params) {
        String location = createResponse.header("Location");
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put(location + "/number-of-guests")
                .then().log().all()
                .extract();
    }

    public static void 주문_테이블_생성_완료(ExtractableResponse<Response> response) {
        HttpStatusAssertion.CREATED(response);
    }

    public static void 주문_테이블_응답(ExtractableResponse<Response> response) {
        HttpStatusAssertion.OK(response);
    }
}
