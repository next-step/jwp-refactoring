package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class MenuGroupAcceptanceTestUtils {
    private static final String MENU_GROUP_PATH = "/api/menu-groups";

    private MenuGroupAcceptanceTestUtils() {}

    public static ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get(MENU_GROUP_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(String name) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new MenuGroupRequest(name))
                .when().post(MENU_GROUP_PATH)
                .then().log().all()
                .extract();
    }

    public static MenuGroupResponse 메뉴_그룹_등록되어_있음(String name) {
        ExtractableResponse<Response> response = 메뉴_그룹_생성_요청(name);
        메뉴_그룹_생성됨(response);
        return response.as(MenuGroupResponse.class);
    }

    public static void 메뉴_그룹_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 메뉴_그룹_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 메뉴_그룹_목록_포함됨(ExtractableResponse<Response> response, String... productNames) {
        List<String> actual = response.jsonPath()
                .getList("name", String.class);
        assertThat(actual).containsExactly(productNames);
    }
}
