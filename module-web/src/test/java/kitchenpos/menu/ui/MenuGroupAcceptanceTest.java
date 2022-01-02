package kitchenpos.menu.ui;

import static kitchenpos.common.AcceptanceFixture.*;
import static kitchenpos.fixture.MenuGroupAcceptanceFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.*;
import java.util.stream.*;

import org.junit.jupiter.api.*;

import io.restassured.response.*;
import kitchenpos.common.*;
import kitchenpos.menu.dto.*;

@DisplayName("메뉴그룹 관련(인수 테스트)")
class MenuGroupAcceptanceTest extends AcceptanceTest {
    private MenuGroupRequest 일인_세트;
    private MenuGroupResponse 일인_세트_등록됨;
    private MenuGroupResponse 패밀리_세트_등록됨;

    @DisplayName("메뉴그룹 조회")
    @Test
    void findTest() {
        일인_세트_등록됨 = 메뉴_그룹_생성_요청(MenuGroupRequest.from("1인 세트")).as(MenuGroupResponse.class);
        패밀리_세트_등록됨 = 메뉴_그룹_생성_요청(MenuGroupRequest.from("패밀리 세트")).as(MenuGroupResponse.class);
        ExtractableResponse<Response> 메뉴_그룹_조회됨 = 메뉴_그룹_조회_요청();
        OK_응답_잘_받았음(메뉴_그룹_조회됨);
        메뉴_그룹_조회_확인(메뉴_그룹_조회됨, 일인_세트_등록됨, 패밀리_세트_등록됨);
    }

    @DisplayName("메뉴 그룹 생성하기")
    @Test
    void createTest() {
        일인_세트 = MenuGroupRequest.from("1인 세트");
        ExtractableResponse<Response> 메뉴_그룹_생성됨 = 메뉴_그룹_생성_요청(일인_세트);
        CREATE_응답_잘_받음(메뉴_그룹_생성됨);
        메뉴_그룹_생성_확인(메뉴_그룹_생성됨, 일인_세트);
    }

    @DisplayName("메뉴명 없이 생성시 실패함")
    @Test
    void failTest() {
        일인_세트 = MenuGroupRequest.from("");
        ExtractableResponse<Response> 메뉴_그룹_생성됨 = 메뉴_그룹_생성_요청(일인_세트);
        BAD_REQUEST_응답_잘_받았음(메뉴_그룹_생성됨);
    }

    private void 메뉴_그룹_생성_확인(ExtractableResponse<Response> actual, MenuGroupRequest menuGroupRequest) {
        MenuGroupResponse response = actual.as(MenuGroupResponse.class);
        assertThat(response.getName()).isEqualTo(menuGroupRequest.getName());
    }

    private void 메뉴_그룹_조회_확인(ExtractableResponse<Response> actual, MenuGroupResponse... expected) {
        List<String> expectedNames = Arrays.stream(expected)
            .map(MenuGroupResponse::getName)
            .collect(Collectors.toList());

        List<MenuGroupResponse> response = actual.jsonPath()
            .getList(".", MenuGroupResponse.class);

        List<String> responseNames = response.stream()
            .map(MenuGroupResponse::getName)
            .collect(Collectors.toList());

        assertThat(responseNames).containsAll(expectedNames);
    }
}
