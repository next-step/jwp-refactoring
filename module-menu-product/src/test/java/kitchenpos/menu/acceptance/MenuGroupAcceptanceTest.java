package kitchenpos.menu.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.MenuProductAcceptanceTest;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("메뉴 그룹 관련 기능")
public class MenuGroupAcceptanceTest extends MenuProductAcceptanceTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    /**
     * Feature: 메뉴 그룹을 관리한다.
     * <p>
     * Scenario: 메뉴 그룹 관리
     * <p>
     * When 메뉴 그룹 등록 요청
     * <p>
     * Then 메뉴 그룹 등록됨
     * <p>
     * When 메뉴 그룹 목록 조회 요청
     * <p>
     * Then 메뉴 그룹 목록이 조회됨
     */
    @DisplayName("메뉴 그룹을 관리")
    @Test
    void 메뉴그룹_관리() {
        ExtractableResponse<Response> response;

        // when 메뉴그룹 등록 요청
        response = 메뉴그룹_등록_요청("빅맥세트");

        // then 메뉴그룹 등록됨
        메뉴그룹_등록됨(response);
        MenuGroupResponse 빅맥세트 = response.as(MenuGroupResponse.class);

        // when 메뉴그룹 목록 조회 요청
        response = 메뉴그룹_목록_조회();

        // then 메뉴그룹 목록이 조회됨
        메뉴그룹_목록_조회됨(response);

        // then 메뉴그룹 목록이 조회됨
        메뉴그룹_목록_포함됨(response, Arrays.asList(빅맥세트));
    }

    public static ExtractableResponse<Response> 메뉴그룹_등록_요청(String name) {
        MenuGroupRequest request = new MenuGroupRequest(name);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴그룹_등록_요청(MenuGroup menuGroup) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuGroup)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴그룹_목록_조회() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menu-groups")
                .then().log().all()
                .extract();
    }


    public static void 메뉴그룹_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 메뉴그룹_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 메뉴그룹_목록_포함됨(ExtractableResponse<Response> response, List<MenuGroupResponse> expectedMenuGroups) {
        List<Long> resultMenuGroupIds = response.jsonPath().getList(".", MenuGroup.class).stream()
                .map(MenuGroup::getId)
                .collect(Collectors.toList());

        List<Long> expectedMenuGroupIds = expectedMenuGroups.stream()
                .map(MenuGroupResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultMenuGroupIds).containsAll(expectedMenuGroupIds);
    }
}
