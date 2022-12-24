package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.springframework.http.MediaType;

public class MenuGroupAcceptanceTestFixture {
    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(MenuGroupRequest menuGroupRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuGroupRequest)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static MenuGroupResponse 메뉴_그룹_생성되어있음(MenuGroupRequest menuGroupRequest) {
        return 메뉴_그룹(메뉴_그룹_생성_요청(menuGroupRequest));
    }

    public static MenuGroupResponse 메뉴_그룹(ExtractableResponse<Response> response) {
        return response.jsonPath().getObject("", MenuGroupResponse.class);
    }
}
