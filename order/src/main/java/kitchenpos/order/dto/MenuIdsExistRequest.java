package kitchenpos.order.dto;

import java.util.List;

public class MenuIdsExistRequest {
    private List<Long> menuIds;

    public MenuIdsExistRequest() {
    }

    private MenuIdsExistRequest(List<Long> menuIds) {
        this.menuIds = menuIds;
    }

    public static MenuIdsExistRequest of(List<Long> menuIds) {
        return new MenuIdsExistRequest(menuIds);
    }

    public List<Long> getMenuIds() {
        return menuIds;
    }
}
