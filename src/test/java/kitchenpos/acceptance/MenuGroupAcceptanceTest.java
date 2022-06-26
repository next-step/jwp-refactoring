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

public class MenuGroupAcceptanceTest extends AcceptanceTest {

    private static final String API_URL = "/api/menu-groups";

    @Test
    @DisplayName("메뉴 그룹 생성 및 조회 시나리오")
    void menuGroupTest() {
        ExtractableResponse<Response> 메뉴_그룹_생성_요청_결과 = 메뉴_그룹_생성_요청("추천메뉴");

        메뉴_그룹_생성됨(메뉴_그룹_생성_요청_결과);

        ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청_결과 = 메뉴_그룹_목록_조회_요청();

        메뉴_그룹_목록_조회됨(메뉴_그룹_목록_조회_요청_결과);
    }

    private static ExtractableResponse<Response> 메뉴_그룹_생성_요청(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuGroup)
                .when().post(API_URL)
                .then().log().all()
                .extract();
    }

    private static void 메뉴_그룹_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private static ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(API_URL)
                .then().log().all()
                .extract();
    }

    private static void 메뉴_그룹_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static MenuGroup 메뉴_그룹_등록_되어_있음(String name) {
        ExtractableResponse<Response> response = 메뉴_그룹_생성_요청(name);

        return response.as(MenuGroup.class);
    }
}
