package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import org.springframework.http.MediaType;

public class MenuRestAssured {

    public static ExtractableResponse<Response> 메뉴_생성_요청(Menu menu) {
        return RestAssured
            .given().log().all()
            .body(menu)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/api/menus")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .when().get("/api/menus")
            .then().log().all()
            .extract();
    }
}
