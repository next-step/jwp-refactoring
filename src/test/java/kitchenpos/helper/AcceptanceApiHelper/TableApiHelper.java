package kitchenpos.helper.AcceptanceApiHelper;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import kitchenpos.dto.request.OrderTableRequest;
import org.springframework.http.MediaType;

public class TableApiHelper {

    public static ExtractableResponse<Response> 빈테이블_생성하기() {
        OrderTableRequest 테이블 = new OrderTableRequest();
        테이블.setNumberOfGuests(0);
        테이블.setEmpty(true);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(테이블)
            .when().post("/api/tables")
            .then().log().all().
            extract();
    }

    public static ExtractableResponse<Response> 테이블_리스트_조회하기() {
        return RestAssured
            .given().log().all()
            .when().get("/api/tables")
            .then().log().all().
            extract();
    }

    public static ExtractableResponse<Response> 유휴테이블_여부_설정하기(String 유휴_여부, long 테이블_번호) {
        Map<String, String> 요청전문 = new HashMap<>();
        요청전문.put("empty", 유휴_여부);

        return RestAssured
            .given().log().all()
            .body(요청전문)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/api/tables/" + 테이블_번호 + "/empty")
            .then().log().all().
            extract();
    }

    public static ExtractableResponse<Response> 테이블_손님_인원_설정하기(Integer 손님_인원_수, long 테이블_번호) {
        Map<String, Integer> 요청전문 = new HashMap<>();
        요청전문.put("numberOfGuests", 손님_인원_수);

        return RestAssured
            .given().log().all()
            .body(요청전문)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/api/tables/" + 테이블_번호 + "/number-of-guests")
            .then().log().all().
            extract();
    }
}
