package kitchenpos.menugroup.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuGroupAcceptanceTest extends AcceptanceTest {

    private static final String API_URL = "/api/menu-groups";

    private static ExtractableResponse<Response> 메뉴_그룹_생성_요청(String name) {
        MenuGroupRequest menuGroupRequest = MenuGroupRequest.of(name);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuGroupRequest)
                .when().post(API_URL)
                .then().log().all()
                .extract();
    }

    public static MenuGroupResponse 메뉴_그룹_등록_되어_있음(String name) {
        ExtractableResponse<Response> response = 메뉴_그룹_생성_요청(name);

        return response.as(MenuGroupResponse.class);
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

    @Test
    @DisplayName("메뉴 관리 기능 (메뉴그룹 생성, 메뉴 그룹 조회)")
    void menuGroupTest() {
        ExtractableResponse<Response> 메뉴_그룹_생성_요청_결과 = 메뉴_그룹_생성_요청("추천메뉴");

        메뉴_그룹_생성됨(메뉴_그룹_생성_요청_결과);

        ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청_결과 = 메뉴_그룹_목록_조회_요청();

        메뉴_그룹_목록_조회됨(메뉴_그룹_목록_조회_요청_결과);
    }
}
