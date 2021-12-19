package kitchenpos.fixtures;

import kitchenpos.domain.MenuGroup;

import java.util.Arrays;
import java.util.List;

/**
 * packageName : kitchenpos.fixtures
 * fileName : MenuGroupFixtures
 * author : haedoang
 * date : 2021/12/17
 * description :
 */
public class MenuGroupFixtures {
    public static MenuGroup createMenuGroup(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }

    public static List<MenuGroup> createMenuGroups(MenuGroup... menuGroups) {
        return Arrays.asList(menuGroups);
    }
}