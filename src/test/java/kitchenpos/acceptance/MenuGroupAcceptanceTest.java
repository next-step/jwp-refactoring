package kitchenpos.acceptance;

import static kitchenpos.acceptance.step.MenuGroupAcceptanceStep.*;
import static kitchenpos.application.fixture.MenuGroupFixture.요청_메뉴그룹_치킨류;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴그룹 관리 기능")
class MenuGroupAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("메뉴그룹을 관리한다.")
    void 메뉴그룹_관리() {
        // given
        MenuGroupRequest 치킨류 = 요청_메뉴그룹_치킨류();

        // when
        ExtractableResponse<Response> 메뉴그룹_등록_결과 = 메뉴그룹_등록_요청(치킨류);
        // then
        MenuGroupResponse 등록된_메뉴그룹 = 메뉴그룹_등록됨(메뉴그룹_등록_결과, 치킨류);

        // when
        ExtractableResponse<Response> 메뉴그룹_목록조회_결과 = 메뉴그룹_조회_요청();
        // then
        메뉴그룹_목록조회_됨(메뉴그룹_목록조회_결과, 등록된_메뉴그룹);
    }
}
