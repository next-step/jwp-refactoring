package kitchenpos.fixture;

import kitchenpos.domain.Name;
import kitchenpos.domain.menu.MenuGroup;

public class MenuGroupFixture {
    public static MenuGroup 그룹1;
    public static MenuGroup 그룹2;

    public static void cleanUp() {
        그룹1 = new MenuGroup(1L, new Name("그룹1"));
        그룹2 = new MenuGroup(2L, new Name("그룹2"));
    }
}
