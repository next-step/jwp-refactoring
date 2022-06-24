package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {
    public static MenuGroup 추천_메뉴 = create(1L, "추천 메뉴");
    public static MenuGroup 강력_추천_메뉴 = create(2L, "강력_추천_메뉴");

    public static MenuGroup create(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);

        return menuGroup;
    }
}
