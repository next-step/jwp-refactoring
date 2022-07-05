package kitchenpos.menu.domain;

import static org.junit.jupiter.api.Assertions.*;

class MenuGroupTest {

    public static MenuGroup 메뉴_그룹_생성(String name) {
        return new MenuGroup(name);
    }

    public static MenuGroup 메뉴_그룹_생성(Long menuId, String name) {
        return new MenuGroup(menuId, name);
    }
}
