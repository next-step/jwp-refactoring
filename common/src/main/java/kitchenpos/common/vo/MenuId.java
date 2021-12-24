package kitchenpos.common.vo;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public final class MenuId {
    @Column(name = "menu_id")
    private final Long id;

    protected MenuId() {
        this.id = null;
    }

    private MenuId(Long id) {
        this.id = id;
    }

    public static MenuId of(Long id) {
        return new MenuId(id);
    }

    public Long value() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof MenuId)) {
            return false;
        }
        MenuId id = (MenuId) o;
        return Objects.equals(this.id, id.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
