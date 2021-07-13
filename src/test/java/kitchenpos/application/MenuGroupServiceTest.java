package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.request.MenuGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MenuGroupServiceTest {
    private final String NEW_MENU_GROUP_NAME = "세번째메뉴";

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴그룹을 생성한다")
    @Test
    void create() {
        MenuGroup menuGroup = menuGroupService.create(new MenuGroupRequest(NEW_MENU_GROUP_NAME));
        MenuGroup newMenuGroup = menuGroupService.findById(menuGroup.getId());

        assertThat(newMenuGroup.getName()).isEqualTo(NEW_MENU_GROUP_NAME);
    }

    @DisplayName("메뉴그룹을 조회한다")
    @Test
    void list() {
        menuGroupService.create(new MenuGroupRequest(NEW_MENU_GROUP_NAME));

        List<String> menuGroupNames = menuGroupService.list().stream()
            .map(MenuGroup::getName)
            .collect(Collectors.toList());

        assertThat(menuGroupNames).contains(NEW_MENU_GROUP_NAME);
    }
}