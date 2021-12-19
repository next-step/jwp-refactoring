package kitchenpos.menu.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;

@DisplayName("메뉴 그룹 관련 기능")
class MenuGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void createMenuGroup() {
        // when
        ExtractableResponse<Response> response = 메뉴_그룹_생성_요청("한마리 메뉴");

        // then
        메뉴_그룹_생성됨(response);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void findAllMenuGroup() {
        // given
        List<MenuGroupResponse> createdResponses = Arrays.asList(
            메뉴_그룹_생성_요청("한마리 메뉴").as(MenuGroupResponse.class),
            메뉴_그룹_생성_요청("두마리 메뉴").as(MenuGroupResponse.class));

        // when
        ExtractableResponse<Response> response = 메뉴_그룹_목록_조회_요청();

        // then
        메뉴_그룹_목록_조회됨(response, createdResponses);

    }

    static ExtractableResponse<Response> 메뉴_그룹_생성_요청(String name) {
        MenuGroupRequest request = new MenuGroupRequest(name);
        return RestAssured
            .given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/api/menu-groups")
            .then().log().all().extract();
    }

    private ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/api/menu-groups")
            .then().log().all().extract();
    }

    private void 메뉴_그룹_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 메뉴_그룹_목록_조회됨(ExtractableResponse<Response> response,
        List<MenuGroupResponse> responses) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(new TypeRef<List<MenuGroupResponse>>() {
        }))
            .containsExactlyElementsOf(responses);
    }
}
