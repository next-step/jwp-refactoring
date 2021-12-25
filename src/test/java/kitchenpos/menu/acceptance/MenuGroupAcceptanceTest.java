package kitchenpos.menu.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import kitchenpos.common.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.testfixtures.acceptance.MenuGroupAcceptanceFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴 그룹 등록")
    @Test
    void create() {
        //given
        MenuGroupRequest 추천메뉴정보 = MenuGroupAcceptanceFixtures.메뉴그룹_정의("추천메뉴");

        //when
        ResponseEntity<MenuGroupResponse> 메뉴그룹_생성_결과 = MenuGroupAcceptanceFixtures.메뉴그룹_생성_요청(
            추천메뉴정보);

        //then
        메뉴그룹_생성_정상_확인(메뉴그룹_생성_결과);
        메뉴그룹_생성_세부내용_확인(메뉴그룹_생성_결과, 추천메뉴정보);
    }

    @DisplayName("메뉴 그룹 조회")
    @Test
    void list() {
        //given
        MenuGroupResponse 추천메뉴 = MenuGroupAcceptanceFixtures.메뉴그룹_생성_요청(
            MenuGroupAcceptanceFixtures.메뉴그룹_정의("추천메뉴")).getBody();
        MenuGroupResponse 베스트메뉴 = MenuGroupAcceptanceFixtures.메뉴그룹_생성_요청(
            MenuGroupAcceptanceFixtures.메뉴그룹_정의("베스트메뉴")).getBody();

        //when
        ResponseEntity<List<MenuGroupResponse>> 조회_결과 = MenuGroupAcceptanceFixtures.메뉴그룹_전체_조회_요청();

        //then
        조회_정상(조회_결과);
        조회목록_정상(조회_결과, Arrays.asList(추천메뉴, 베스트메뉴));
    }

    private void 조회_정상(ResponseEntity<List<MenuGroupResponse>> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private void 메뉴그룹_생성_정상_확인(ResponseEntity<MenuGroupResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    private void 메뉴그룹_생성_세부내용_확인(ResponseEntity<MenuGroupResponse> response,
        MenuGroupRequest menuGroupRequest) {
        MenuGroupResponse createdMenuGroup = response.getBody();
        assertThat(createdMenuGroup.getName()).isEqualTo(menuGroupRequest.getName());
    }

    private void 조회목록_정상(ResponseEntity<List<MenuGroupResponse>> response,
        List<MenuGroupResponse> expectedMenuGroups) {
        List<MenuGroupResponse> menuGroupResponses = response.getBody();
        assertThat(menuGroupResponses).containsAll(expectedMenuGroups);
    }
}
