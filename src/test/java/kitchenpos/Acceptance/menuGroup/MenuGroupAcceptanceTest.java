package kitchenpos.Acceptance.menuGroup;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.Acceptance.AcceptanceTest;
import kitchenpos.menu.MenuGenerator;
import kitchenpos.menuGroup.MenuGroupGenerator;
import kitchenpos.menuGroup.dto.MenuGroupResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.menu.MenuGenerator.*;
import static kitchenpos.menuGroup.MenuGroupGenerator.*;
import static org.assertj.core.api.Assertions.assertThat;

public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴 그룹 생성 요청 시 정상 생성되어야 한다")
    @Test
    void menuGroupCreateTest() {
        // given
        String 메뉴_그룹_이름 = "메뉴 그룹";

        // when
        ExtractableResponse<Response> 메뉴_그룹_생성_결과 = 메뉴_그룹_생성_API_호출(메뉴_그룹_이름);

        // then
        메뉴_그룹_정상_생성됨(메뉴_그룹_생성_결과, 메뉴_그룹_이름);
    }

    @DisplayName("메뉴 그룹 목록 조회 시 정상 조회되어야 한다")
    @Test
    void menuGroupFindAllTest() {
        // given
        List<String> 메뉴_그룹_이름들 = Arrays.asList("메뉴 그룹 1", "메뉴 그룹 2", "메뉴 그룹 3", "메뉴 그룹 4", "메뉴 그룹 5");
        메뉴_그룹_이름들.forEach(MenuGroupGenerator::메뉴_그룹_생성_API_호출);

        // when
        ExtractableResponse<Response> 메뉴_그룹_목록_조회_결과 = 메뉴_그룹_목록_조회_API_호출();

        // then
        메뉴_그룹_목록_정상_조회됨(메뉴_그룹_목록_조회_결과, 메뉴_그룹_이름들);
    }

    @DisplayName("없는 메뉴 그룹을 조회하면 예외가 발생해야 한다")
    @Test
    void findProductByNotSavedTest() {
        // when
        ExtractableResponse<Response> 메뉴_그룹_조회_결과 = 메뉴_그룹_조회_API_요청(-1L);

        // then
        메뉴_그룹_조회_실패됨(메뉴_그룹_조회_결과);
    }

    @DisplayName("정상 메뉴 그룹을 조회하면 정상 조회되어야 한다")
    @Test
    void findMenuGroupTest() {
        // given
        String 메뉴_그룹_이름 = "메뉴 그룹";
        Long 메뉴_그룹_아이디 = 메뉴_그룹_생성_API_호출(메뉴_그룹_이름).body().jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> 메뉴_그룹_조회_결과 = 메뉴_그룹_조회_API_요청(메뉴_그룹_아이디);

        // then
        메뉴_그룹_조회_성공됨(메뉴_그룹_조회_결과, 메뉴_그룹_아이디, 메뉴_그룹_이름);
    }

    void 메뉴_그룹_정상_생성됨(ExtractableResponse<Response> response, String expectedName) {
        MenuGroupResponse menuGroup = response.as(MenuGroupResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_CREATED);
        assertThat(menuGroup.getName()).isEqualTo(expectedName);
        assertThat(menuGroup.getId()).isNotNull();
    }

    void 메뉴_그룹_목록_정상_조회됨(ExtractableResponse<Response> response, List<String> expectedNames) {
        List<String> names = response.body().jsonPath().getList("name", String.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(names).containsAll(expectedNames);
    }

    private void 메뉴_그룹_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    private void 메뉴_그룹_조회_성공됨(ExtractableResponse<Response> response, Long id, String name) {
        MenuGroupResponse menuGroup = response.as(MenuGroupResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(menuGroup.getId()).isEqualTo(id);
        assertThat(menuGroup.getName()).isEqualTo(name);
    }
}
