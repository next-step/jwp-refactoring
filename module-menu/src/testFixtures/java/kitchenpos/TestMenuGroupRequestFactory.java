package kitchenpos;

import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.menuGroup.dto.MenuGroupRequest;

public class TestMenuGroupRequestFactory {
    public static MenuGroupRequest create(MenuGroup menuGroup) {
        return new MenuGroupRequest(menuGroup.getName().toString());
    }
}
