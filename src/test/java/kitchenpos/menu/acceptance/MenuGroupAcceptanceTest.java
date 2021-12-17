package kitchenpos.menu.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MenuGroupAcceptanceTest extends AcceptanceTest {
    private static final String URL = "/api/menu-groups";

    @Test
    @DisplayName("메뉴 그룹을 관리한다.")
    void manageMenuGroup() {
        // 메뉴 그룹 등록 요청
        ExtractableResponse<Response> saveResponse = 메뉴그룹_등록_요청("메뉴그룹");
        // 메뉴 그룹 등록 됨
        메뉴그룹_등록_됨(saveResponse);

        // 메뉴 그룹 조회 요청
        ExtractableResponse<Response> response = 메뉴그룹_목록_조회_요청();
        // 메뉴 그룹 조회 됨
        메뉴그룹_목록_조회_됨(response, "메뉴그룹");
    }

    public static ExtractableResponse<Response> 메뉴그룹_등록_요청(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(menuGroup)
            .when().post(URL)
            .then().log().all()
            .extract();
    }

    public static void 메뉴그룹_등록_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 메뉴그룹_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get(URL)
            .then().log().all()
            .extract();
    }

    public static void 메뉴그룹_목록_조회_됨(ExtractableResponse<Response> response, String... expected) {
        List<MenuGroup> menuGroups = response.jsonPath().getList(".", MenuGroup.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(menuGroups).extracting(MenuGroup::getName).containsExactly(expected);
    }

    public static MenuGroup 메뉴그룹_등록_되어있음(String name) {
        return 메뉴그룹_등록_요청(name).as(MenuGroup.class);
    }
}
