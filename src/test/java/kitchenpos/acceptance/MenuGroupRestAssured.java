package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;
import org.springframework.http.MediaType;

public class MenuGroupRestAssured {
    private MenuGroupRestAssured() {
    }

    public static ExtractableResponse<Response> 메뉴_그룹_등록되어_있음(Long id, String name) {
        return 메뉴_그룹_생성_요청(id, name);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(Long id, String name) {
        return RestAssured
                .given().log().all()
                .body(MenuGroup.of(id, name))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menu-groups")
                .then().log().all()
                .extract();
    }
}
