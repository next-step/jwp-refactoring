package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupName;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;

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
