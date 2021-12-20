package kitchenpos.acceptance;

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

@DisplayName("메뉴 그룹 관련 기능")
class MenuGroupAcceptanceTest extends AcceptanceTest {

    private static final String API_URL = "/api/menu-groups";

    @DisplayName("메뉴 그룹을 관리한다.")
    @Test
    void manageMenuGroup() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("추천_메뉴_그룹");

        // when
        ExtractableResponse<Response> 메뉴_그룹_생성_응답 = 메뉴_그룹_생성_요청(menuGroup);
        // then
        메뉴_그룹_생성됨(메뉴_그룹_생성_응답);

        // when
        ExtractableResponse<Response> 메뉴_그룹_목록_조회_응답 = 메뉴_그룹_목록_조회_요청();
        // then
        메뉴_그룹_목록_조회됨(메뉴_그룹_목록_조회_응답);
    }

    private static ExtractableResponse<Response> 메뉴_그룹_생성_요청(MenuGroup params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(API_URL)
                .then().log().all()
                .extract();
    }

    private void 메뉴_그룹_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(API_URL)
                .then().log().all()
                .extract();
    }

    private void 메뉴_그룹_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static MenuGroup 메뉴_그룹_등록되어_있음(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);

        ExtractableResponse<Response> response = 메뉴_그룹_생성_요청(menuGroup);
        return response.as(MenuGroup.class);
    }
}
