package kitchenpos.acceptance;

import static kitchenpos.acceptance.step.MenuAcceptanceStep.*;
import static kitchenpos.acceptance.step.MenuGroupAcceptanceStep.메뉴그룹_등록됨;
import static kitchenpos.acceptance.step.ProductAcceptanceStep.상품_등록됨;
import static kitchenpos.acceptance.step.ProductAcceptanceStep.양념치킨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Collections;
import kitchenpos.AcceptanceTest;
import kitchenpos.dto.menu.MenuGroupRequest;
import kitchenpos.dto.menu.MenuGroupResponse;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuProductRequests;
import kitchenpos.dto.menu.MenuRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 관리 기능")
class MenuAcceptanceTest extends AcceptanceTest {


    @Test
    @DisplayName("메뉴 관리 한다.")
    void 메뉴_관리() {
        // given
        MenuRequest 치킨메뉴 = 치킨메뉴();

        // when
        ExtractableResponse<Response> 메뉴_등록_결과 = 메뉴_등록_요청(치킨메뉴);
        // then
        Long 등록된_메뉴_번호 = 메뉴_등록_검증(메뉴_등록_결과, 치킨메뉴);

        // when
        ExtractableResponse<Response> 메뉴_목록조회_결과 = 메뉴_목록조회_요청();
        // then
        메뉴_목록조회_검증(메뉴_목록조회_결과, 등록된_메뉴_번호);
    }

    private MenuRequest 치킨메뉴() {
        Long 치킨번호 = 상품_등록됨(양념치킨());
        MenuGroupResponse 치킨류 = 메뉴그룹_등록됨(new MenuGroupRequest("치킨류"));

        return new MenuRequest("치킨메뉴", 16000, 치킨류.getId(), new MenuProductRequests(
            Collections.singletonList(new MenuProductRequest(치킨번호, 1L))));
    }
}
