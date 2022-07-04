package kitchenpos.__fixture__;

import kitchenpos.domain.MenuGroup;

public class MenuGroupTestFixture {
    public static MenuGroup 메뉴_그룹_생성(final Long id, final String name) {
        return new MenuGroup(id, name);
    }
}
