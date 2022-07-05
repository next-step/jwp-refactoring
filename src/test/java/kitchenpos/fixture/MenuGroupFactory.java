package kitchenpos.fixture;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupName;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;

public class MenuGroupFactory {
    public static MenuGroup createMenuGroup(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup(id, MenuGroupName.from(name));
        return menuGroup;
    }

    public static MenuGroupRequest createMenuGroupRequest(String name) {
        return new MenuGroupRequest(name);
    }

    public static MenuGroupResponse createMenuGroupResponse(Long id, String name) {
        return new MenuGroupResponse(id, name);
    }
}
