package kitchenpos.menu.acceptance.behavior;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuDto;

public class MenuContextBehavior {
    private MenuContextBehavior() {
    }
    public static ExtractableResponse<Response> 메뉴그룹_생성_요청(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        return RestAssured
                .given().contentType(ContentType.JSON).log().all()
                .when().body(params).post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static MenuGroup 메뉴그룹_생성됨(String name) {
        return 메뉴그룹_생성_요청(name).as(MenuGroup.class);
    }

    public static ExtractableResponse<Response> 메뉴그룹_목록조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static List<MenuGroup> 메뉴그룹_목록조회() {
        return 메뉴그룹_목록조회_요청().jsonPath().getList("$", MenuGroup.class);
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(MenuDto menuDto) {
        return RestAssured
                .given().contentType(ContentType.JSON).log().all()
                .when().body(menuDto).post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static MenuDto 메뉴_생성됨(MenuDto menuDto) {
        return 메뉴_생성_요청(menuDto).as(MenuDto.class);
    }

    public static ExtractableResponse<Response> 메뉴_목록조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }

    public static List<MenuDto> 메뉴_목록조회() {
        return 메뉴_목록조회_요청().jsonPath().getList("$", MenuDto.class);
    }


}
