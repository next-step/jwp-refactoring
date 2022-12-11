package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupTestFixture {

    public static MenuGroup createMenuGroup(Long id, String name) {
        return MenuGroup.of(id, name);
    }

    public static MenuGroup createMenuGroup(String name) {
        return MenuGroup.of(null, name);
    }

    public static MenuGroup 중국집_1인_메뉴_세트() {
        return createMenuGroup(1L, "중국집_1인_메뉴_세트");
    }
}
