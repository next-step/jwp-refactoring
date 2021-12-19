package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static final MenuGroup 추천_메뉴_그룹 = create(1L, "추천_메뉴_그룹");
    public static final MenuGroup 시즌_메뉴_그룹 = create(2L, "시즌_메뉴_그룹");

    private MenuGroupFixture() {
        throw new UnsupportedOperationException();
    }

    public static MenuGroup create(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);

        return menuGroup;
    }
}
