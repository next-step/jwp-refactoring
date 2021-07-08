package kitchenpos.menugroup.acceptance;

import static kitchenpos.utils.acceptan.RequestHelper.*;
import static kitchenpos.utils.acceptan.ResponseHelper.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.AcceptanceTest;

@DisplayName("메뉴 그룹 인수 테스트")
public class MenuGroupAcceptanceTest extends AcceptanceTest {

    private static final String 국밥 = "국밥";

    @DisplayName("사용자는 메뉴 그룹을 생성 할 수 있다.")
    @Test
    void create() {
        // given
        // when
        ExtractableResponse<Response> 메뉴_그룹_생성_요청_응답 = 메뉴_그룹_생성_요청(국밥);

        // then
        생성_요청_확인(메뉴_그룹_생성_요청_응답);
    }

    @DisplayName("사용자는 메뉴 그룹 전체를 조회 할 수 있다.")
    @Test
    void list() {
        // given

        // when
        ExtractableResponse<Response> 메뉴_그룹_생성_요청_응답 = 메뉴_그룹_생성_요청(국밥);
        //then
        생성_요청_확인(메뉴_그룹_생성_요청_응답);

        // when
        ExtractableResponse<Response> 메뉴_그룹_조회_요청_응답 = 메뉴_그룹_조회_요청();
        // then
        메뉴_그룹_조회_요청_응답_확인(메뉴_그룹_조회_요청_응답, 국밥);
    }
}
