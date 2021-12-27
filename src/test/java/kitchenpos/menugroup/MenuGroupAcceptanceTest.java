package kitchenpos.menugroup;

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

import java.util.List;

import static kitchenpos.menugroup.fixtures.MenuGroupFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName : kitchenpos.acceptance
 * fileName : MenuGroupAcceptanceTest
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
@DisplayName("메뉴그룹 인수테스트")
public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴그룹을 등록한다")
    @Test
    void create() {
        // given
        final MenuGroupRequest request = 메뉴그룹요청("두마리메뉴");

        // when
        final ExtractableResponse<Response> response = 메뉴그룹_등록_요청함(request);

        // then
        메뉴그룹_등록됨(response);
    }

    @DisplayName("메뉴그룹 리스트를 조회한다.")
    @Test
    void list() {
        // given
        메뉴그룹_등록되어있음(메뉴그룹요청("한마리메뉴"));
        메뉴그룹_등록되어있음(메뉴그룹요청("두마리메뉴"));

        // when
        final ExtractableResponse<Response> response = 메뉴그룹_리스트_요청함();

        // then
        메뉴그룹_리스트_조회됨(response);
    }

    private ExtractableResponse<Response> 메뉴그룹_리스트_요청함() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static MenuGroupResponse 메뉴그룹_등록되어있음(MenuGroupRequest request) {
        final ExtractableResponse<Response> response = 메뉴그룹_등록_요청함(request);
        return response.jsonPath().getObject("", MenuGroupResponse.class);
    }

    public static ExtractableResponse<Response> 메뉴그룹_등록_요청함(MenuGroupRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    private void 메뉴그룹_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotNull();
    }

    private void 메뉴그룹_리스트_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final List<MenuGroupResponse> menuGroupResponses = response.jsonPath().getList("", MenuGroupResponse.class);
        assertThat(menuGroupResponses.size()).isGreaterThanOrEqualTo(0);
    }
}
