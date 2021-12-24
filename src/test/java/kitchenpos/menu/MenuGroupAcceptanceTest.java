package kitchenpos.menu;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.utils.RestTestApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("메뉴 그룹 인수테스트")
class MenuGroupAcceptanceTest extends AcceptanceTest {

    private static final String URI = "/api/menu-groups";

    private MenuGroupRequest 두마리_메뉴그룹;
    private MenuGroupRequest 한마리_메뉴그룹;

    @BeforeEach
    public void setUp() {
        super.setUp();

        두마리_메뉴그룹 = new MenuGroupRequest("두마리메뉴");
        한마리_메뉴그룹 = new MenuGroupRequest("한마리메뉴");
    }

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void createMenuGroup() {
        // when
        ExtractableResponse<Response> response = 메뉴그룹_생성_요청(두마리_메뉴그룹);

        // then
        메뉴그룹_생성됨(response);
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() {
        // given
        ExtractableResponse<Response> 응답_두마리메뉴그룹 = 메뉴그룹_생성_요청(두마리_메뉴그룹);
        ExtractableResponse<Response> 응답_한마리메뉴그룹 = 메뉴그룹_생성_요청(한마리_메뉴그룹);

        // when
        ExtractableResponse<Response> 메뉴그룹_목록_응답 = 메뉴그룹_목록_요청();

        // then
        메뉴그룹_목록_응답됨(메뉴그룹_목록_응답);
        메뉴그룹_목록_포함됨(메뉴그룹_목록_응답, Arrays.asList(응답_두마리메뉴그룹, 응답_한마리메뉴그룹));
    }

    public static ExtractableResponse<Response> 메뉴그룹_등록되어_있음(MenuGroupRequest menuGroupRequest) {
        ExtractableResponse<Response> response = 메뉴그룹_생성_요청(menuGroupRequest);
        메뉴그룹_생성됨(response);
        return response;
    }

    private static ExtractableResponse<Response> 메뉴그룹_생성_요청(MenuGroupRequest menuGroupRequest) {
        return RestTestApi.post(URI, menuGroupRequest);
    }

    private static void 메뉴그룹_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private ExtractableResponse<Response> 메뉴그룹_목록_요청() {
        return RestTestApi.get(URI);
    }

    private void 메뉴그룹_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 메뉴그룹_목록_포함됨(ExtractableResponse<Response> response,
        List<ExtractableResponse<Response>> expectedResponses) {

        List<Long> responseIds = response.jsonPath().getList(".", MenuGroupResponse.class).stream()
            .map(MenuGroupResponse::getId)
            .collect(Collectors.toList());

        List<Long> expectedIds = expectedResponses.stream()
            .map(expectedResponse -> expectedResponse.as(MenuGroupResponse.class))
            .map(MenuGroupResponse::getId)
            .collect(Collectors.toList());

        assertThat(responseIds).containsAll(expectedIds);
    }
}
