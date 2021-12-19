package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.IntegrationServiceTest;
import kitchenpos.domain.MenuGroup;

class MenuGroupServiceTest extends IntegrationServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void create() {
        // given
        final MenuGroup menuGroup = makeMenuGroup("두마리메뉴");

        // when
        final MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertThat(savedMenuGroup.getId()).isNotNull();
        assertThat(savedMenuGroup.getName()).isEqualTo("두마리메뉴");
    }

    @Test
    void list() {
        // given
        final MenuGroup menuGroup = makeMenuGroup("두마리메뉴");
        menuGroupService.create(menuGroup);

        // when
        final List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).isNotEmpty();
        assertThat(menuGroups.get(0).getName()).isEqualTo("두마리메뉴");
    }

    public static MenuGroup makeMenuGroup(final String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }
}
