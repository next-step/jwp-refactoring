package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴그룹 관련 기능")
public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("메뉴그룹을 등록한다.")
    void createMenuGroup() {
        // when
        ExtractableResponse<Response> response = 메뉴_그룹_등록_요청("추천메뉴");

        // then
        메뉴_그룹_등록됨(response);
    }

    @Test
    @DisplayName("메뉴그룹을 조회한다.")
    void getMenuGroup() {
        // when
        ExtractableResponse<Response> response = 메뉴_그룹_조회_요청();

        // then
        메뉴_그룹_조회됨(response);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_등록_요청(String name) {
        return RestAssured
                .given().log().all()
                .body(MenuGroupRequest.of(name))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menu-groups")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 메뉴_그룹_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menu-groups")
                .then().log().all().extract();
    }

    public static void 메뉴_그룹_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 메뉴_그룹_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
