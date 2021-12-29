package kitchenpos.fixture;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup 생성(String name) {
        return new MenuGroup(name);
    }

    public static MenuGroup 치킨류(){
        return 생성("치킨류");
    }

    public static MenuGroup 피자류(){
        return 생성("피자류");
    }
}
