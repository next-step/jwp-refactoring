package kitchenpos.menu.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.MenuRequest;
import org.springframework.http.MediaType;

public class MenuAcceptanceUtils {
    private MenuAcceptanceUtils() {
    }

    public static ExtractableResponse<Response> 메뉴_등록되어_있음(MenuRequest request) {
        return 메뉴_생성_요청(request);
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(MenuRequest request) {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }
}
