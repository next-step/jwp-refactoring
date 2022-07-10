package kitchenpos.order.generator;

import kitchenpos.menuGroup.domain.MenuGroup;

public class MenuGroupGenerator {

    public static MenuGroup 메뉴_그룹_생성(String name) {
        return new MenuGroup(name);
    }
}
