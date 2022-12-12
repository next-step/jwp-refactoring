package kitchenpos.menu.acceptance;

import static kitchenpos.menu.domain.MenuGroupTestFixture.generateMenuGroup;
import static kitchenpos.menu.domain.MenuGroupTestFixture.generateMenuGroupRequest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import org.springframework.http.MediaType;

public class MenuGroupRestAssured {

    public static ExtractableResponse<Response> 메뉴_그룹_등록되어_있음(String name) {
        return 메뉴_그룹_생성_요청(generateMenuGroupRequest(name));
    }

    public static ExtractableResponse<Response> 메뉴_그룹_등록되어_있음(MenuGroupRequest menuGroupRequest) {
        return 메뉴_그룹_생성_요청(menuGroupRequest);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(MenuGroupRequest menuGroupRequest) {
        return RestAssured
                .given().log().all()
                .body(menuGroupRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
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
