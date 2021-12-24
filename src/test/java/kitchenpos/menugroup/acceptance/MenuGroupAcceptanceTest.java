package kitchenpos.menugroup.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.RestAssuredFixture.*;

@DisplayName("메뉴 그룹 관련 기능")
public class MenuGroupAcceptanceTest extends AcceptanceTest {

    private static final String API_URL = "/api/menu-groups";

    @DisplayName("메뉴 그룹을 관리한다.")
    @Test
    void manageMenuGroup() {
        // given
        MenuGroup menuGroup = MenuGroup.of("추천_메뉴_그룹");

        // when
        ExtractableResponse<Response> 메뉴_그룹_생성_응답 = 메뉴_그룹_생성_요청(menuGroup);
        // then
        메뉴_그룹_생성됨(메뉴_그룹_생성_응답);

        // when
        ExtractableResponse<Response> 메뉴_그룹_목록_조회_응답 = 메뉴_그룹_목록_조회_요청();
        // then
        메뉴_그룹_목록_조회됨(메뉴_그룹_목록_조회_응답);
    }

    private static ExtractableResponse<Response> 메뉴_그룹_생성_요청(MenuGroup params) {
        return 생성_요청(API_URL, params);
    }

    private void 메뉴_그룹_생성됨(ExtractableResponse<Response> response) {
        생성됨_201_CREATED(response);
    }

    private ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
        return 목록_조회_요청(API_URL);
    }

    private void 메뉴_그룹_목록_조회됨(ExtractableResponse<Response> response) {
        성공_200_OK(response);
    }

    public static MenuGroup 메뉴_그룹_등록되어_있음(String name) {
        MenuGroup menuGroup = MenuGroup.of(name);

        return 메뉴_그룹_생성_요청(menuGroup).as(MenuGroup.class);
    }
}
