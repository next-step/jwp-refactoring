package kitchenpos.menugroup.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import kitchenpos.AcceptanceTest;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;

@DisplayName("메뉴 그룹 인수 테스트")
public class MenuGroupAcceptanceTest extends AcceptanceTest {

    private static final String 국밥 = "국밥";

    @DisplayName("사용자는 메뉴 그룹을 생성 할 수 있다.")
    @Test
    void create() {
        // given
        // when
        ExtractableResponse<Response> 메뉴_그룹_생성_요청_응답 = 메뉴_그룹_생성_요청(국밥);

        // then
        메뉴_그룹_생성_요청_응답_확인(메뉴_그룹_생성_요청_응답);
    }

    @DisplayName("사용자는 메뉴 그룹 전체를 조회 할 수 있다.")
    @Test
    void list() {
        // given

        // when
        ExtractableResponse<Response> 메뉴_그룹_생성_요청_응답 = 메뉴_그룹_생성_요청(국밥);
        //then
        메뉴_그룹_생성_요청_응답_확인(메뉴_그룹_생성_요청_응답);

        // when
        ExtractableResponse<Response> 메뉴_그룹_조회_요청_응답 = 메뉴_그룹_조회_요청();
        // then
        메뉴_그룹_조회_요청_응답_확인(메뉴_그룹_조회_요청_응답);
    }

    private void 메뉴_그룹_조회_요청_응답_확인(ExtractableResponse<Response> 메뉴_그룹_조회_요청_응답) {
        MenuGroupResponse menuGroupResponse = 메뉴_그룹_조회_요청_응답.as(MenuGroupResponse.class);
        assertThat(menuGroupResponse.getMenuGroupResponses().get(0).getName()).isEqualTo("국밥");
    }

    private ExtractableResponse<Response> 메뉴_그룹_조회_요청() {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/api/menu-groups/")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 메뉴_그룹_생성_요청(String 요청_메뉴_그룹) {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest(요청_메뉴_그룹);
        return RestAssured.given().log().all()
                .body(menuGroupRequest)
                .contentType(ContentType.JSON)
                .when().post("/api/menu-groups/")
                .then().log().all()
                .extract();
    }

    private void 메뉴_그룹_생성_요청_응답_확인(ExtractableResponse<Response> 메뉴_그룹_생성_요청_응답) {
        assertThat(메뉴_그룹_생성_요청_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
