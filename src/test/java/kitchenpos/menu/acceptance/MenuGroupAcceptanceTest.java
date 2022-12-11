package kitchenpos.menu.acceptance;


import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴그룹 관련 인수 테스트")
public class MenuGroupAcceptanceTest extends AcceptanceTest {

    /**
     * When : 메뉴 그룹 생성을 요청한다.
     * Then : 메뉴 그룹이 생성된다.
     */
    @DisplayName("메뉴 그룹 생성 인수 테스트")
    @Test
    void createMenuGroup() {
        // when
        ExtractableResponse<Response> response = 메뉴그룹_생성_요청("세마리 치킨");

        // then
        메뉴그룹_생성됨(response);
    }

    /**
     * Given : 메뉴그룹이 생성되어 있다.
     * When : 메뉴그룹 조회를 요청하면,
     * Then : 메뉴그룹 목록을 응답한다.
     */
    @DisplayName("메뉴그룹 조회 인수 테스트")
    @Test
    void findMenuGroups() {
        // when
        ExtractableResponse<Response> response = 메뉴그룹_조회_요청();

        // then
        메뉴그룹_조회됨(response);
    }

    public static ExtractableResponse<Response> 메뉴그룹_생성_요청(String name) {
        MenuGroup menuGroupRequest = new MenuGroup(name);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuGroupRequest)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴그룹_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static void 메뉴그룹_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 메뉴그룹_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
