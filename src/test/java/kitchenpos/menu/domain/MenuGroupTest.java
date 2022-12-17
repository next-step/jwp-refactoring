package kitchenpos.menu.domain;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupTest {

    public static MenuGroup 메뉴그룹_생성(Long id, String name) {
        return new MenuGroup.Builder()
                .id(id)
                .name(name)
                .build();
    }
}