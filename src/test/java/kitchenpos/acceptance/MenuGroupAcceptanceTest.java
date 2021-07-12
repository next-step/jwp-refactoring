package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

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

public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴 그룹 생성 통합 테스트")
    @Test
    void createTest() {
        // given
        MenuGroupRequest request = new MenuGroupRequest("메뉴그룹");

        // when
        ExtractableResponse<Response> response = 메뉴_그룹_생성_요청(request);

        // then
        MenuGroupResponse actual = 메뉴_그룹_생성_성공(response);
        assertThat(actual).isNotNull()
                          .extracting(MenuGroupResponse::getName)
                          .isEqualTo("메뉴그룹");
    }

    @DisplayName("전체 메뉴 그룹 조회 통합 테스트")
    @Test
    void listTest() {
        // when
        ExtractableResponse<Response> response = 전체_메뉴_그룹_조회_요청();

        // then
        전체_메뉴_그룹_조회_성공(response);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(final MenuGroupRequest request) {
        // when
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menu-groups")
                .then().log().all().extract();
    }

    public static MenuGroupResponse 메뉴_그룹_생성_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(MenuGroupResponse.class);
    }

    public static ExtractableResponse<Response> 전체_메뉴_그룹_조회_요청() {
        // when
        return RestAssured.given().log().all()
                .when().get("/api/menu-groups")
                .then().log().all().extract();
    }

    public static void 전체_메뉴_그룹_조회_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

}
