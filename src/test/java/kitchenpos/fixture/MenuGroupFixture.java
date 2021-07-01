package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {
    public static MenuGroup 그룹1;
    public static MenuGroup 그룹2;

    public static void cleanUp() {
        그룹1 = new MenuGroup(1L, "그룹1");
        그룹2 = new MenuGroup(2L, "그룹2");
    }
}
