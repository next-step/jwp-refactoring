package kitchenpos.menu.acceptance;

public class MenuId {
    private final long menuId;

    public MenuId(long menuId) {
        this.menuId = menuId;
    }

    public long value() {
        return menuId;
    }
}
