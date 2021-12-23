package kitchenpos.application.fixture;

import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.dto.menu.MenuGroupRequest;

public class MenuGroupFixture {

    private MenuGroupFixture() {
    }

    public static MenuGroup 메뉴그룹_치킨류() {
        return MenuGroup.of("치킨류");
    }

    public static MenuGroupRequest 요청_메뉴그룹_치킨류() {
        return new MenuGroupRequest("메뉴그룹치킨류");
    }
}
