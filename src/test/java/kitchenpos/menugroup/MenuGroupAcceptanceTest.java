package kitchenpos.menugroup;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 그룹 관련 기능")
public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("시나리오1: 메뉴 그룹을 등록하고 목록을 조회할 수 있다.")
    public void scenarioTest() throws Exception {
        // when 메뉴_그룹_등록_요청
        ExtractableResponse<Response> 메뉴_그룹_등록 = 메뉴_그룹_등록_요청("후라이드 치킨 두마리");
        // then 메뉴_그룹_등록됨
        메뉴_그룹_등록됨(메뉴_그룹_등록);

        // when 메뉴_그룹_목록_조회_요청
        ExtractableResponse<Response> 메뉴_그룹_목록 = 메뉴_그룹_목록_조회_요청();
        // then 메뉴_그룹_목록_조회됨
        메뉴_그룹_목록_조회됨(메뉴_그룹_목록);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_등록_요청(String name) {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest(name);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuGroupRequest)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static void 메뉴_그룹_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static void 메뉴_그룹_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".", MenuGroupResponse.class)
                .stream()
                .map(MenuGroupResponse::getName)
                .anyMatch(s -> s.equals("후라이드 치킨 두마리")))
                .isTrue();
    }
}
