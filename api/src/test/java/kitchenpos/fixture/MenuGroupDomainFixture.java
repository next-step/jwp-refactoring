package kitchenpos.fixture;

import kitchenpos.menu.domain.menugroup.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;

public class MenuGroupDomainFixture {

    public static MenuGroup 일인_세트 = menuGroup("일인 세트");
    public static MenuGroupRequest 일인_세트_요청 = MenuGroupRequest.from(일인_세트.getName());

    public static MenuGroup 패밀리_세트 = menuGroup("패밀리 세트");
    public static MenuGroupRequest 패밀리_세트_요청 = MenuGroupRequest.from(패밀리_세트.getName());

    public static MenuGroup menuGroup(String name) {
        return new MenuGroup(name);
    }


}
