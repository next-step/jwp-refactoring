package kitchenpos.application.fixture;

import kitchenpos.dto.menu.MenuGroupRequest;

public class MenuGroupFixture {

    private MenuGroupFixture() {
    }


    public static MenuGroupRequest 메뉴그룹_치킨류() {
        return new MenuGroupRequest("메뉴그룹치킨류");
    }
}
