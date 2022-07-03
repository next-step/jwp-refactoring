package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup 메뉴_그룹_데이터_생성(Long id, String name) {
        return new MenuGroup(id, name);
    }
}
