package kitchenpos.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 그룹 관련 기능")
class MenuGroupAcceptanceTest extends AcceptanceTest {

    @Test
    void createMenuGroup() {
        MenuGroup menuGroup = MenuGroup.of(5L, "핫메뉴");

        ExtractableResponse<Response> response = 메뉴_그룹_생성_요청(menuGroup);

        메뉴_그룹_생성됨(response);
    }

    @Test
    void getMenuGroupList() {
        ExtractableResponse<Response> response = 메뉴_그룹_목록_조회_요청();

        메뉴_그룹_목록_조회됨(response);
        메뉴_그룹_목록_포함됨(response, Arrays.asList("두마리메뉴", "한마리메뉴", "순살파닭두마리메뉴", "신메뉴"));
    }

    private static ExtractableResponse<Response> 메뉴_그룹_생성_요청(MenuGroup menuGroup) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuGroup)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    private static void 메뉴_그룹_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private static void 메뉴_그룹_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 메뉴_그룹_목록_포함됨(ExtractableResponse<Response> response, List<String> resultNames) {
        List<String> menuGroupNames = response.jsonPath().getList(".", MenuGroup.class).stream()
                .map(MenuGroup::getName)
                .collect(Collectors.toList());

        assertThat(menuGroupNames).containsAll(resultNames);
    }
}
