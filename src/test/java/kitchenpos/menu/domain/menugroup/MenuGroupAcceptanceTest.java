package kitchenpos.menu.domain.menugroup;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.utils.AcceptanceFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("인수 테스트 - 메뉴그룹 관리")
class MenuGroupAcceptanceTest extends AcceptanceTest {
    private MenuGroupRequest 일인_세트;
    private MenuGroupRequest 패밀리_세트;

    private MenuGroupResponse 일인_세트_등록됨;
    private MenuGroupResponse 패밀리_세트_등록됨;


    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(MenuGroupRequest menuGroupRequest) {
        return post("/new/api/menu-groups", menuGroupRequest);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_조회_요청() {
        return get("/new/api/menu-groups");
    }

    private void 메뉴_그룹_생성됨(ExtractableResponse<Response> actual) {
        MenuGroupResponse response = actual.as(MenuGroupResponse.class);
        assertThat(response.getName()).isEqualTo(일인_세트.getName());
    }

    private void 메뉴_그룹_조회됨(ExtractableResponse<Response> actual, MenuGroupResponse... expected) {
        List<MenuGroupResponse> response = actual.jsonPath().getList(".",MenuGroupResponse.class);
        assertThat(response).contains(expected);

    }

    @Nested
    @DisplayName("메뉴그룹 생성")
    class CreateMenuGroup {
        @Test
        @DisplayName("성공")
        public void 메뉴_그룹_생성() {
            // given
            일인_세트 = MenuGroupRequest.from("1인 세트");

            // when
            ExtractableResponse<Response> actual = 메뉴_그룹_생성_요청(일인_세트);

            // then
            응답_CREATE(actual);
            메뉴_그룹_생성됨(actual);
        }

        @Test
        @DisplayName("실패 - 그룹 메뉴명 없음.")
        public void 메뉴_그룹_생성_실패() {
            // given
            일인_세트 = MenuGroupRequest.from("");

            // when
            ExtractableResponse<Response> actual = 메뉴_그룹_생성_요청(일인_세트);

            // then
            응답_BAD_REQUEST(actual);
        }
    }

    @Test
    @DisplayName("메뉴그룹 조회")
    public void 메뉴_그룹_조회() {
        // given
        일인_세트_등록됨 = 메뉴_그룹_생성_요청(MenuGroupRequest.from("1인 세트")).as(MenuGroupResponse.class);
        패밀리_세트_등록됨 =  메뉴_그룹_생성_요청(MenuGroupRequest.from("패밀리 세트")).as(MenuGroupResponse.class);

        // when
        ExtractableResponse<Response> actual = 메뉴_그룹_조회_요청();

        응답_OK(actual);
        메뉴_그룹_조회됨(actual, 일인_세트_등록됨, 패밀리_세트_등록됨);
    }



}
