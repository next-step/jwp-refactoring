package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup 메뉴_그룹_기본 = create(1L, "메뉴 그룹 기본");
    public static MenuGroup 메뉴_그룹_요일 = create(2L, "메뉴 그룹 요일");

    public static MenuGroup create(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }
}
