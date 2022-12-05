package kitchenpos.acceptance;

import static kitchenpos.domain.MenuGroupTestFixture.generateMenuGroup;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;
import org.springframework.http.MediaType;

public class MenuGroupRestAssured {

    public static ExtractableResponse<Response> 메뉴_그룹_등록되어_있음(Long id, String name) {
        return 메뉴_그룹_생성_요청(generateMenuGroup(id, name));
    }

    public static ExtractableResponse<Response> 메뉴_그룹_등록되어_있음(MenuGroup menuGroup) {
        return 메뉴_그룹_생성_요청(menuGroup);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(MenuGroup menuGroup) {
        return RestAssured
                .given().log().all()
                .body(menuGroup)
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
