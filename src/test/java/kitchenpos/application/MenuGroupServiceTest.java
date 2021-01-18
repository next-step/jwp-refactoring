package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 그룹 서비스")
public class MenuGroupServiceTest extends ServiceTestBase {
    private final MenuGroupService menuGroupService;

    @Autowired
    public MenuGroupServiceTest(MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("추천메뉴");
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        assertThat(savedMenuGroup.getId()).isNotNull();
    }

    @DisplayName("모든 메뉴 그룹을 조회한다")
    @Test
    void findAll() {
        menuGroupService.create(createMenuGroup("추천메뉴"));
        menuGroupService.create(createMenuGroup("점심특선"));

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups.size()).isEqualTo(2);
        List<String> menuGroupNames = menuGroups.stream()
                .map(MenuGroup::getName)
                .collect(Collectors.toList());
        assertThat(menuGroupNames).contains("추천메뉴", "점심특선");
    }

    public static MenuGroup createMenuGroup(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }
}
