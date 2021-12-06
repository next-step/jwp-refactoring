package kitchenpos.application;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import org.springframework.http.MediaType;

public class MenuFactory {

    public static ExtractableResponse<Response> 메뉴_생성_요청(Menu params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/menus")
                .then().log().all().
                extract();
    }

    public static ExtractableResponse<Response> 메뉴_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menus")
                .then().log().all().
                extract();
    }
}
