package kitchenpos.menu;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.dto.MenuGroupRequest;
import org.springframework.http.MediaType;

public class MenuGroupFixture {

    public static ExtractableResponse<Response> 메뉴_그룹_등록(String name) {
        MenuGroupRequest menuGroup = new MenuGroupRequest(name);

        return RestAssured
            .given().log().all()
            .body(menuGroup)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/api/menu-groups")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 메뉴_그룹_목록_조회() {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/menu-groups")
            .then().log().all()
            .extract();
    }
}
