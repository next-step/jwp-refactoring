package kitchenpos.application.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup 고기_메뉴그룹 = new MenuGroup();
    public static MenuGroup 야채_메뉴그룹 = new MenuGroup();

    static {
        init();
    }

    public static void init() {
        고기_메뉴그룹.setId(1L);
        고기_메뉴그룹.setName("고기 메뉴그룹");

        야채_메뉴그룹.setId(2L);
        야채_메뉴그룹.setName("야채 메뉴그룹");
    }

    private MenuGroupFixture() {}
}
