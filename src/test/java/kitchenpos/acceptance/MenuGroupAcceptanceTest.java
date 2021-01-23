package kitchenpos.acceptance;

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

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 그룹 관리")
public class MenuGroupAcceptanceTest extends AcceptanceTest {
    @DisplayName("메뉴 그룹을 관리한다")
    @Test
    void manage() {
        메뉴_그룹_생성();
        메뉴_그룹_조회();
    }

    private void 메뉴_그룹_생성() {
        MenuGroupRequest request = createRequest();
        ExtractableResponse<Response> createdResponse = 생성_요청(request);

        생성됨(createdResponse, request);
    }

    private void 메뉴_그룹_조회() {
        ExtractableResponse<Response> selectedResponse = 조회_요청();

        조회됨(selectedResponse);
    }

    public static MenuGroupRequest createRequest() {
        return new MenuGroupRequest("추천메뉴");
    }

    public static ExtractableResponse<Response> 생성_요청(MenuGroupRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static void 생성됨(ExtractableResponse<Response> response, MenuGroupRequest request) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        MenuGroupResponse menuGroup = response.as(MenuGroupResponse.class);
        assertThat(menuGroup.getName()).isEqualTo(request.getName());
    }

    public static ExtractableResponse<Response> 조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static void 조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<MenuGroupResponse> menuGroups = Arrays.asList(response.as(MenuGroupResponse[].class));
        assertThat(menuGroups.size()).isEqualTo(1);
    }
}
