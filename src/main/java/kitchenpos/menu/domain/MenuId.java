package kitchenpos.menu.domain;

public class MenuId {
    private Long menuId;

    protected MenuId() {
    }

    public MenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getMenuId() {
        return menuId;
    }
}
