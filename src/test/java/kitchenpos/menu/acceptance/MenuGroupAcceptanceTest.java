package kitchenpos.menu.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("메뉴 그룹 관련 기능 인수 테스트")
public class MenuGroupAcceptanceTest extends AcceptanceTest {

    /**
     * When 메뉴 그룹 생성을 요청하면
     * Then 메뉴 그룹이 생성된다.
     */
    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void createMenu() {
        // when
        ExtractableResponse<Response> response =MenuGroupAcceptance.create_menu_group("양식");

        // then
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
    }

    /**
     * When 메뉴 그룹의 이름을 빈 값으로 하여 메뉴 그룹 생성을 요청하면
     * Then 메뉴 그룹을 생성할 수 없다.
     */
    @DisplayName("빈 값의 이름을 입력하여 메뉴 그룹을 생성한다.")
    @Test
    void createMenuWithEmptyName() {
        // when
        ExtractableResponse<Response> response =MenuGroupAcceptance.create_menu_group(null);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 메뉴 그룹을 등록하고
     * When 메뉴 그룹 목록 조회를 요청하면
     * Then 메뉴 그룹 목록이 조회된다.
     */
    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() {
        // given
        MenuGroupAcceptance.create_menu_group("양식");
        MenuGroupAcceptance.create_menu_group("중식");

        // when
        ExtractableResponse<Response> response = MenuGroupAcceptance.menu_group_list_has_been_queried();

        // then
        assertThat(response.jsonPath().getList(".", MenuGroupResponse.class)).hasSize(2);
    }
}
