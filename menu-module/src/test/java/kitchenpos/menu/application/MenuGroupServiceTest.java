package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.IntegrationServiceTest;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;

public class MenuGroupServiceTest extends IntegrationServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void create() {
        // given
        final MenuGroupRequest menuGroup = makeMenuGroupRequest("두마리메뉴");

        // when
        final MenuGroupResponse savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertThat(savedMenuGroup.getId()).isNotNull();
        assertThat(savedMenuGroup.getName()).isEqualTo("두마리메뉴");
    }

    @Test
    void list() {
        // given
        final MenuGroupRequest menuGroup = makeMenuGroupRequest("두마리메뉴");
        menuGroupService.create(menuGroup);

        // when
        final List<MenuGroupResponse> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).isNotEmpty();
        assertThat(menuGroups.get(0).getName()).isEqualTo("두마리메뉴");
    }

    public static MenuGroupRequest makeMenuGroupRequest(final String name) {
        return new MenuGroupRequest(name);
    }
}
