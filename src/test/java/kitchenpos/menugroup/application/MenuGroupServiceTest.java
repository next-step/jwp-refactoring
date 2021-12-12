package kitchenpos.menugroup.application;

import kitchenpos.ServiceTest;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("메뉴 그룹 서비스 테스트")
class MenuGroupServiceTest extends ServiceTest {

    @Test
    @DisplayName("메뉴 그룹을 등록한다.")
    void create() {
        // given
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("세마리메뉴");

        // when
        MenuGroupResponse savedMenuGroupResponse = menuGroupService.create(menuGroupRequest);

        // then
        assertAll(
                () -> assertThat(savedMenuGroupResponse.getId()).isNotNull(),
                () -> assertThat(savedMenuGroupResponse.getName()).isEqualTo(menuGroupRequest.getName())
        );
    }

    @Test
    @DisplayName("메뉴 그룹의 목록을 조회한다.")
    void list() {
        // when
        List<MenuGroupResponse> menuGroupResponses = menuGroupService.list();

        // then
        assertThat(menuGroupResponses.size()).isPositive();
    }
}
