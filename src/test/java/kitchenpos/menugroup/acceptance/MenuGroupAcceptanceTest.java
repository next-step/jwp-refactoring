package kitchenpos.menugroup.acceptance;

import static kitchenpos.menugroup.acceptance.MenuGroupAcceptanceTestHelper.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;

@DisplayName("메뉴 그룹 관리")
public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() {
        // when
        ExtractableResponse<Response> 메뉴_그룹_등록_요청_응답 = 메뉴그룹_등록_요청("분식");

        // then
        메뉴그룹_등록됨(메뉴_그룹_등록_요청_응답);
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() {
        // when
        ExtractableResponse<Response> 메뉴_그룹_목록_요청_응답 = 메뉴_그룹_목록_요청();

        // then
        메뉴그룹_조회됨(메뉴_그룹_목록_요청_응답);
    }
}
