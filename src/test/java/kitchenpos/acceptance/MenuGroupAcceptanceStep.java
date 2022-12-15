package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.dto.MenuGroupRequest;
import org.springframework.http.MediaType;

public class MenuGroupAcceptanceStep {
    private MenuGroupAcceptanceStep() {
    }

    public static ExtractableResponse<Response> 메뉴_그룹_등록되어_있음(MenuGroupRequest request) {
        return 메뉴_그룹_생성_요청(request);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(MenuGroupRequest request) {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menu-groups")
                .then().log().all()
                .extract();
    }
}
