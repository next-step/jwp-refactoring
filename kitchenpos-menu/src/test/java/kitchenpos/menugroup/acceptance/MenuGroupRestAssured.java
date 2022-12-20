package kitchenpos.menugroup.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import org.springframework.http.MediaType;

public class MenuGroupRestAssured {

    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(MenuGroupRequest menuGroupRequest) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(menuGroupRequest)
            .when().post("/api/menu-groups")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .when().get("/api/menu-groups")
            .then().log().all()
            .extract();
    }
}
