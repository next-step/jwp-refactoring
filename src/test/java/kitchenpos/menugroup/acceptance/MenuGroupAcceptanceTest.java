package kitchenpos.menugroup.acceptance;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.MenuGroup;

@DisplayName("메뉴 그룹 관리")
public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() {
        ExtractableResponse 메뉴_그룹_등록_요청_응답 = 메뉴_그룹_등록_요청("분식");

        메뉴_그룹_등록됨(메뉴_그룹_등록_요청_응답);
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() {
        ExtractableResponse 메뉴_그룹_목록_요청_응답 = 메뉴_그룹_목록_요청();

        메뉴_그룹_조회됨(메뉴_그룹_목록_요청_응답);
    }

    private ExtractableResponse 메뉴_그룹_목록_요청() {
        return RestAssured
            .given().log().all()
            .when().get("/api/menu-groups")
            .then().log().all().extract();
    }

    private void 메뉴_그룹_조회됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 메뉴_그룹_등록_요청(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(menuGroup)
            .when().post("/api/menu-groups")
            .then().log().all().extract();
    }

    private void 메뉴_그룹_등록됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
