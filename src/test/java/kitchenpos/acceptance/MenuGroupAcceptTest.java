package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.acceptance.step.MenuGroupAcceptStep.메뉴_그룹_등록_요청;
import static kitchenpos.acceptance.step.MenuGroupAcceptStep.메뉴_그룹_등록_확인;
import static kitchenpos.acceptance.step.MenuGroupAcceptStep.메뉴_그룹_목록_조회_요청;
import static kitchenpos.acceptance.step.MenuGroupAcceptStep.메뉴_그룹_조회_확인;

@DisplayName("메뉴 그룹 인수테스트")
class MenuGroupAcceptTest extends AcceptanceTest {
    @DisplayName("메뉴 그룹을 관리한다")
    @Test
    void 메뉴_그룹을_관리한다() {
        // given
        MenuGroup 메뉴_그룹_등록_요청_데이터 = new MenuGroup();
        메뉴_그룹_등록_요청_데이터.setName("추천메뉴");

        // when
        ExtractableResponse<Response> 메뉴_그룹_등록_응답 = 메뉴_그룹_등록_요청(메뉴_그룹_등록_요청_데이터);

        // then
        MenuGroup 등록된_메뉴_그룹 = 메뉴_그룹_등록_확인(메뉴_그룹_등록_응답, 메뉴_그룹_등록_요청_데이터);

        // when
        ExtractableResponse<Response> 메뉴_그룹_목록_조회_응답 = 메뉴_그룹_목록_조회_요청();

        // then
        메뉴_그룹_조회_확인(메뉴_그룹_목록_조회_응답, 등록된_메뉴_그룹);
    }
}
