package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;
import org.springframework.http.MediaType;

public class MenuGroupAcceptanceSupport {

    public static ExtractableResponse<Response> 메뉴_그룹_등록요청(MenuGroup menuGroup) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(menuGroup)
            .when().post("/api/menu-groups")
            .then().log().all().
            extract();
    }
}
