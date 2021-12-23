package kitchenpos.application.acceptance.menugroup;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.dto.menugroup.MenuGroupRequest;
import org.springframework.http.MediaType;

public class MenuGroupFactory {

    public static ExtractableResponse<Response> 메뉴그룹_생성_요청(MenuGroupRequest params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/menu-groups")
                .then().log().all().
                extract();
    }

    public static ExtractableResponse<Response> 메뉴그룹_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menu-groups")
                .then().log().all().
                extract();
    }
}
