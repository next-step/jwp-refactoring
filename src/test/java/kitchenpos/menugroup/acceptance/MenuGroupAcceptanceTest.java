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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 그룹 관련 기능")
public class MenuGroupAcceptanceTest extends AcceptanceTest {
    public static final String MENU_GROUP_NAME01 = "점심특선메뉴";
    public static final String MENU_GROUP_NAME02 = "어린이전용메뉴";

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void create() {
        // when
        ExtractableResponse<Response> 등록된_메뉴_그룹 = 메뉴_그룹_등록되어_있음(MENU_GROUP_NAME01);

        // then
        메뉴_그룹_생성_검증됨(등록된_메뉴_그룹);
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() {
        // given
        ExtractableResponse<Response> 등록된_메뉴_그룹1 = 메뉴_그룹_등록되어_있음(MENU_GROUP_NAME01);
        ExtractableResponse<Response> 등록된_메뉴_그룹2 = 메뉴_그룹_등록되어_있음(MENU_GROUP_NAME02);

        // when
        ExtractableResponse<Response> 메뉴_그룹_목록 = 메뉴_그룹_목록_조회_요청();

        // then
        메뉴_그룹_목록_검증됨(메뉴_그룹_목록);
        메뉴_그룹_목록_포함됨(메뉴_그룹_목록, Arrays.asList(등록된_메뉴_그룹1, 등록된_메뉴_그룹2));
    }

    public static ExtractableResponse<Response> 메뉴_그룹_등록되어_있음(String name) {
        return 메뉴_그룹_생성_요청(new MenuGroupRequest(name));
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(MenuGroupRequest request) {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static void 메뉴_그룹_생성_검증됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static void 메뉴_그룹_목록_검증됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 메뉴_그룹_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> resultLineIds = response.jsonPath().getList(".", MenuGroupResponse.class).stream()
                .map(MenuGroupResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    public static MenuGroupResponse 메뉴_그룹_가져옴(ExtractableResponse<Response> response) {
        return response.as(MenuGroupResponse.class);
    }
}
