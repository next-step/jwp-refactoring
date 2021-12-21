package kitchenpos.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.domain.menu.Menu;

@Embeddable
public final class MenuId {
    @Column(name = "menu_id")
    private final Long menuId;

    protected MenuId() {
        this.menuId = null;
    }

    private MenuId(Long menuId) {
        this.menuId = menuId;
    }

    public static MenuId of(Long menuId) {
        return new MenuId(menuId);
    }
    public static MenuId of(Menu menu) {
        return new MenuId(menu.getId());
    }

    public Long value() {
        return this.menuId;
    }
}
