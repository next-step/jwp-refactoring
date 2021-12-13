package kitchenpos.product;

import static kitchenpos.product.step.MenuGroupAcceptanceStep.메뉴_그룹_등록_되어_있음;
import static kitchenpos.product.step.MenuGroupAcceptanceStep.메뉴_그룹_등록_됨;
import static kitchenpos.product.step.MenuGroupAcceptanceStep.메뉴_그룹_등록_요청;
import static kitchenpos.product.step.MenuGroupAcceptanceStep.메뉴_그룹_목록_조회_됨;
import static kitchenpos.product.step.MenuGroupAcceptanceStep.메뉴_그룹_목록_조회_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.ui.response.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 그룹 관련 기능")
class MenuGroupAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    void create() {
        //given
        String name = "두마리메뉴";

        //when
        ExtractableResponse<Response> response = 메뉴_그룹_등록_요청(name);

        //then
        메뉴_그룹_등록_됨(response, name);
    }

    @Test
    @DisplayName("메뉴 그룹들을 조회할 수 있다.")
    void list() {
        //given
        MenuGroupResponse menuGroup = 메뉴_그룹_등록_되어_있음("두마리메뉴");

        //when
        ExtractableResponse<Response> response = 메뉴_그룹_목록_조회_요청();

        //then
        메뉴_그룹_목록_조회_됨(response, menuGroup);
    }
}
