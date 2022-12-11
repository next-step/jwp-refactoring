package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.fixture.MenuGroupTestFixture.createMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;

public class MenuGroupAcceptanceStep {

    public static ExtractableResponse<Response> 등록된_메뉴_그룹(Long id, String name) {
        return 메뉴_그룹_생성_요청(createMenuGroup(id, name));
    }

    public static ExtractableResponse<Response> 등록된_메뉴_그룹(MenuGroupRequest menuGroup) {
        return 메뉴_그룹_생성_요청(menuGroup);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(MenuGroupRequest menuGroup) {
        return RestAssured
                .given().log().all()
                .body(menuGroup)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static void 메뉴_그룹_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 메뉴_그룹_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 메뉴_그룹_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedMenuGroupIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> resultMenuGroupIds = response.jsonPath().getList(".", MenuGroup.class).stream()
                .map(MenuGroup::getId)
                .collect(Collectors.toList());

        assertThat(resultMenuGroupIds).containsAll(expectedMenuGroupIds);
    }
}
