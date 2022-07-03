package kitchenpos.menuGroup.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menuGroup.dto.MenuGroupRequest;
import kitchenpos.menuGroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MenuGroupAcceptanceTest extends AcceptanceTest {


    @DisplayName("메류 그룹을 생성한다.")
    @Test
    void createMenuGroup() {
        // given
        String name = "메뉴그룹";

        // when
        ExtractableResponse<Response> response = 메뉴_그룹_생성_요청(name);

        // then
        메뉴_그룹_생성_요청_됨(response, name);
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void listMenuGroup() {
        // given
        MenuGroupResponse menuGroupResponse = 메뉴_그룹_생성되어_있음("메뉴 그룹1").as(MenuGroupResponse.class);
        MenuGroupResponse menuGroupResponse1 = 메뉴_그룹_생성되어_있음("메뉴 그룹2").as(MenuGroupResponse.class);
        List<MenuGroupResponse> menuGroupResponses = Arrays.asList(menuGroupResponse, menuGroupResponse1);

        // when
        ExtractableResponse<Response> response = 메뉴_그룹_목록조회_요청();

        // then
        메뉴_그룹_목록조회_요청_됨(response, menuGroupResponses.size());
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(String name) {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest(name);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuGroupRequest)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성되어_있음(String name) {
        return 메뉴_그룹_생성_요청(name);
    }

    private static void 메뉴_그룹_생성_요청_됨(ExtractableResponse<Response> response, String name) {
        MenuGroupResponse menuGroupResponse = response.as(MenuGroupResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(menuGroupResponse.getMenuGroupId()).isNotNull(),
                () -> assertThat(menuGroupResponse.getName()).isEqualTo(name)
        );
    }

    private static ExtractableResponse<Response> 메뉴_그룹_목록조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    private static void 메뉴_그룹_목록조회_요청_됨(ExtractableResponse<Response> response, int size) {
        List<MenuGroupResponse> menuGroupResponses = response.body()
                .jsonPath()
                .getList(".", MenuGroupResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(menuGroupResponses).hasSize(size)
        );
    }

}
