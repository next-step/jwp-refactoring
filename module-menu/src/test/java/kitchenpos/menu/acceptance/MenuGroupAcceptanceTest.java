package kitchenpos.menu.acceptance;

import static kitchenpos.menu.acceptance.MenuGroupAcceptanceSupport.메뉴_그룹_등록됨;
import static kitchenpos.menu.acceptance.MenuGroupAcceptanceSupport.메뉴_그룹_등록요청;
import static kitchenpos.menu.acceptance.MenuGroupAcceptanceSupport.메뉴_그룹목록_조회됨;
import static kitchenpos.menu.acceptance.MenuGroupAcceptanceSupport.메뉴_그룹목록_조회요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.acceptance.utils.AcceptanceTest;
import kitchenpos.menu.dto.request.MenuGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴그룹에 관한 인수 테스트")
class MenuGroupAcceptanceTest extends AcceptanceTest {
    private MenuGroupRequest 메뉴_그룹;
    private MenuGroupRequest 메뉴_그룹2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        메뉴_그룹 = new MenuGroupRequest("치킨 그룹");
        메뉴_그룹2 = new MenuGroupRequest("피자 그룹");
    }

    @DisplayName("메뉴그룹을 등록한다")
    @Test
    void create_test() {
        // when
        ExtractableResponse<Response> response = 메뉴_그룹_등록요청(메뉴_그룹);

        // then
        메뉴_그룹_등록됨(response);
    }

    @DisplayName("모든 메뉴그룹을 조회한다")
    @Test
    void find_test() {
        // given
        메뉴_그룹_등록요청(메뉴_그룹);
        메뉴_그룹_등록요청(메뉴_그룹2);

        // when
        ExtractableResponse<Response> response = 메뉴_그룹목록_조회요청();

        // then
        메뉴_그룹목록_조회됨(response, 2);
    }
}
