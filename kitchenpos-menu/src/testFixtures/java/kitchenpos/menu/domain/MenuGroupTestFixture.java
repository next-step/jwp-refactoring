package kitchenpos.menu.domain;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupTestFixture {

    public static MenuGroup 메뉴_그룹_추천_메뉴() {
        return 메뉴_그룹("추천 메뉴");
    }

    public static MenuGroup 메뉴_그룹(String name) {
        return MenuGroup.from(name);
    }

    public static MenuGroup 메뉴_그룹_추천_메뉴(Long id) {
        return 메뉴_그룹(id, "추천 메뉴");
    }

    public static MenuGroup 메뉴_그룹(Long id, String name) {
        return MenuGroup.from(id, name);
    }
}
