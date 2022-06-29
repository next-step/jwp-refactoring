package kitchenpos.utils.generator;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.MenuGroup;

public class MenuGroupFixtureGenerator {

    private static String NAME = "오늘의 메뉴";
    private static int COUNTER = 0;

    public static MenuGroup generateMenuGroup() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(NAME + COUNTER);
        return menuGroup;
    }

    public static MenuGroup generateMenuGroup(final String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }

    public static List<MenuGroup> generateMenuGroups(int count) {
        List<MenuGroup> menuGroups = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            menuGroups.add(generateMenuGroup());
        }
        return menuGroups;
    }
}
