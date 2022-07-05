package kitchenpos.fixture;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;

public class TestMenuGroupRequestFactory {
    public static MenuGroupRequest create(MenuGroup menuGroup) {
        return new MenuGroupRequest(menuGroup.getName().toString());
    }
}
