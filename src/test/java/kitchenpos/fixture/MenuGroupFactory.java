package kitchenpos.fixture;

import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;

public class MenuGroupFactory {
    public static MenuGroupRequest createMenuGroupRequest(String name) {
        return new MenuGroupRequest(name);
    }

    public static MenuGroupResponse createMenuGroupResponse(Long id, String name) {
        return new MenuGroupResponse(id, name);
    }
}
