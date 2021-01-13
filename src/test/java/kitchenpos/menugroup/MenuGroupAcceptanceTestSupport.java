package kitchenpos.menugroup;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.HttpStatusAssertion;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class MenuGroupAcceptanceTestSupport extends AcceptanceTest {
    public static ExtractableResponse<Response> 메뉴_그룹_등록_요청(Map<String, Object> params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_그룹_등록됨(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        ExtractableResponse<Response> response = 메뉴_그룹_등록_요청(params);
        메뉴_그룹_생성_완료(response);
        return response;
    }

    public static void 메뉴_그룹_생성_완료(ExtractableResponse<Response> response) {
        HttpStatusAssertion.CREATED(response);
    }

    public void 메뉴_그룹_응답(ExtractableResponse<Response> response) {
        HttpStatusAssertion.OK(response);
    }
}
