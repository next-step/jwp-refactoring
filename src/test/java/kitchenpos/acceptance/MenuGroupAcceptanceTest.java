package kitchenpos.acceptance;

import static kitchenpos.acceptance.support.MenuGroupAcceptanceSupport.메뉴_그룹_등록됨;
import static kitchenpos.acceptance.support.MenuGroupAcceptanceSupport.메뉴_그룹_등록요청;
import static kitchenpos.acceptance.support.MenuGroupAcceptanceSupport.메뉴_그룹_등록요청_copy;
import static kitchenpos.acceptance.support.MenuGroupAcceptanceSupport.메뉴_그룹목록_조회됨;
import static kitchenpos.acceptance.support.MenuGroupAcceptanceSupport.메뉴_그룹목록_조회요청;
import static kitchenpos.acceptance.support.MenuGroupAcceptanceSupport.메뉴_그룹목록_조회요청_copy;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴그룹에 관한 인수 테스트")
class MenuGroupAcceptanceTest extends AcceptanceTest {
    private MenuGroup 메뉴_그룹;
    private MenuGroup 메뉴_그룹2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        메뉴_그룹 = MenuGroup.of(null, "치킨 그룹");
        메뉴_그룹2 = MenuGroup.of(null, "피자 그룹");
    }

    @DisplayName("메뉴그룹을 등록한다")
    @Test
    void create_Test() {
        // when
        ExtractableResponse<Response> response = 메뉴_그룹_등록요청(메뉴_그룹);

        // then
        메뉴_그룹_등록됨(response);
    }

    @DisplayName("모든 메뉴그룹을 조회한다")
    @Test
    void find_Test() {
        // given
        메뉴_그룹_등록요청(메뉴_그룹);
        메뉴_그룹_등록요청(메뉴_그룹2);

        // when
        ExtractableResponse<Response> response = 메뉴_그룹목록_조회요청();

        // then
        메뉴_그룹목록_조회됨(response, 2);
    }

    @DisplayName("메뉴그룹을 등록한다")
    @Test
    void create_Test_copy() {
        // when
        ExtractableResponse<Response> response = 메뉴_그룹_등록요청_copy(메뉴_그룹);

        // then
        메뉴_그룹_등록됨(response);
    }

    @DisplayName("모든 메뉴그룹을 조회한다")
    @Test
    void find_Test_copy() {
        // given
        메뉴_그룹_등록요청_copy(메뉴_그룹);
        메뉴_그룹_등록요청_copy(메뉴_그룹2);

        // when
        ExtractableResponse<Response> response = 메뉴_그룹목록_조회요청_copy();

        // then
        메뉴_그룹목록_조회됨(response, 2);
    }
}
