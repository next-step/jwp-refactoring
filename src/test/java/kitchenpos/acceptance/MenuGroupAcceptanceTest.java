package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static kitchenpos.acceptance.MenuGroupAcceptanceFactory.메뉴그룹_등록_요청;
import static kitchenpos.acceptance.MenuGroupAcceptanceFactory.메뉴그룹_등록됨;
import static kitchenpos.acceptance.MenuGroupAcceptanceFactory.메뉴그룹_조회_요청;
import static kitchenpos.acceptance.MenuGroupAcceptanceFactory.메뉴그룹_조회됨;

@DisplayName("메뉴그룹 관련")
public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @Test
    void 메뉴그룹을_등록할_수_있다() {
        ExtractableResponse<Response> 메뉴그룹_등록_결과 = 메뉴그룹_등록_요청("두마리메뉴");

        메뉴그룹_등록됨(메뉴그룹_등록_결과);
    }

    @Test
    void 메뉴그룹을_조회할_수_있다() {
        MenuGroupResponse 두마리메뉴 = 메뉴그룹_등록_요청("두마리메뉴").as(MenuGroupResponse.class);
        MenuGroupResponse 한마리메뉴 = 메뉴그룹_등록_요청("한마리메뉴").as(MenuGroupResponse.class);
        MenuGroupResponse 신메뉴 = 메뉴그룹_등록_요청("신메뉴").as(MenuGroupResponse.class);

        ExtractableResponse<Response> 메뉴그룹_조회_결과 = 메뉴그룹_조회_요청();

        메뉴그룹_조회됨(메뉴그룹_조회_결과,Arrays.asList(두마리메뉴,한마리메뉴,신메뉴));
    }
}
