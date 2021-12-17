package kitchenpos.domain.menu.dto;

import java.util.List;

public class MenuExistRequest {

    private List<Long> menuIds;

    public MenuExistRequest() {
    }

    public MenuExistRequest(List<Long> menuIds) {
        this.menuIds = menuIds;
    }

    public List<Long> getMenuIds() {
        return menuIds;
    }
}
