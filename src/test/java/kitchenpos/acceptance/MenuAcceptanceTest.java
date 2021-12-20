package kitchenpos.acceptance;

import static kitchenpos.acceptance.step.MenuAcceptanceStep.*;
import static kitchenpos.acceptance.step.MenuGroupAcceptanceStep.메뉴그룹_등록됨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Collections;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 관리 기능")
class MenuAcceptanceTest extends AcceptanceTest {

    private MenuGroup 치킨류;
    private Menu 메뉴_치킨;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        치킨류 = 메뉴그룹_등록됨(new MenuGroup("치킨류"));
        메뉴_치킨 = Menu.of("치킨메뉴", 16000, 치킨류.getId(),
            Collections.singletonList(MenuProduct.of(1L, 1L)));
    }

    @Test
    @DisplayName("메뉴 관리 한다.")
    void 메뉴_관리() {
        // when
        ExtractableResponse<Response> 메뉴_등록_결과 = 메뉴_등록_요청(메뉴_치킨);
        // then
        Menu 등록된_메뉴 = 메뉴_등록_검증(메뉴_등록_결과, 메뉴_치킨);

        // when
        ExtractableResponse<Response> 메뉴_목록조회_결과 = 메뉴_목록조회_요청();
        // then
        메뉴_목록조회_검증(메뉴_목록조회_결과, 등록된_메뉴);
    }


}
