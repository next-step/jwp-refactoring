package kitchenpos.vo;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof MenuId)) {
            return false;
        }
        MenuId menuId = (MenuId) o;
        return Objects.equals(this.menuId, menuId.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(menuId);
    }

}
