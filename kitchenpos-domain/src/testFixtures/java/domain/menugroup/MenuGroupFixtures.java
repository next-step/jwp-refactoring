package domain.menugroup;

import api.menugroup.domain.MenuGroup;


public class MenuGroupFixtures {
    public static MenuGroup 메뉴그룹(String name) {
        return new MenuGroup(name);
    }
}
