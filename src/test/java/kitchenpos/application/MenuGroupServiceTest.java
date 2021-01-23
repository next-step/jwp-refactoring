package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupServiceTest extends DomainTestUtils {
    private final String NEW_MENU_GROUP_NAME = "세번째메뉴";

    @DisplayName("메뉴그룹을 생성한다")
    @Test
    void create() {
        MenuGroup menuGroup = menuGroupService.create(new MenuGroup(NEW_MENU_GROUP_NAME));
        MenuGroup newMenuGroup = menuGroupService.findMenuGroupById(menuGroup.getId());

        assertThat(newMenuGroup.getName()).isEqualTo(NEW_MENU_GROUP_NAME);
    }

    @DisplayName("메뉴그룹을 조회한다")
    @Test
    void list() {
        menuGroupService.create(new MenuGroup(NEW_MENU_GROUP_NAME));

        List<String> menuGroupNames = menuGroupService.list().stream()
                .map(MenuGroup::getName)
                .collect(Collectors.toList());

        assertThat(menuGroupNames).contains(NEW_MENU_GROUP_NAME);
    }
}