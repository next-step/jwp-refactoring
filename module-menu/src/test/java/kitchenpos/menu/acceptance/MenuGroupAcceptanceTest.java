package kitchenpos.menu.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.utils.RestAssuredMethods.get;
import static kitchenpos.utils.RestAssuredMethods.post;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴그룹 관련 기능 인수테스트")
public class MenuGroupAcceptanceTest extends AcceptanceTest {
    private MenuGroupRequest 한식_menuGroupRequest;
    private MenuGroupRequest 양식_menuGroupRequest;

    @BeforeEach
    public void setUp() {
        super.setUp();

        한식_menuGroupRequest = MenuGroupRequest.from("한식");
        양식_menuGroupRequest = MenuGroupRequest.from("양식");
    }

    /**
     * Feature: 메뉴그룹 관련 기능
     *
     *   Scenario: 메뉴그룹을 관리
     *     When 한식 메뉴그룹 등록 요청
     *     Then 한식 메뉴그룹 등록됨
     *     When 양식 메뉴그룹 등록 요청
     *     Then 양식 메뉴그룹 등록됨
     *     When 메뉴그룹 조회 요청
     *     Then 한식, 양식 메뉴그룹 조회됨
     */
    @DisplayName("메뉴그룹을 관리한다")
    @Test
    void 메뉴그룹_관리_정상_시나리오() {
        ExtractableResponse<Response> 한식_등록 = 메뉴그룹_등록_요청(한식_menuGroupRequest);
        메뉴그룹_등록됨(한식_등록);

        ExtractableResponse<Response> 양식_등록 = 메뉴그룹_등록_요청(양식_menuGroupRequest);
        메뉴그룹_등록됨(양식_등록);

        ExtractableResponse<Response> 메뉴그룹_목록_조회 = 메뉴그룹_목록_조회_요청();
        메뉴그룹_목록_응답됨(메뉴그룹_목록_조회);
        메뉴그룹_목록_포함됨(메뉴그룹_목록_조회, Arrays.asList(한식_등록, 양식_등록));
    }

    public static ExtractableResponse<Response> 메뉴그룹_등록_요청(MenuGroupRequest params) {
        return post("/api/menu-groups", params);
    }

    public static ExtractableResponse<Response> 메뉴그룹_목록_조회_요청() {
        return get("/api/menu-groups");
    }

    public static void 메뉴그룹_등록됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 메뉴그룹_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 메뉴그룹_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedMenuGroupIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> resultMenuGroupIds = response.jsonPath().getList(".", MenuGroupResponse.class).stream()
                .map(MenuGroupResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultMenuGroupIds).containsAll(expectedMenuGroupIds);
    }

    public static ExtractableResponse<Response> 메뉴그룹_등록되어_있음(String name) {
        return 메뉴그룹_등록_요청(MenuGroupRequest.from(name));
    }
}
