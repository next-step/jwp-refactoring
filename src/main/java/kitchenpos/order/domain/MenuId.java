package kitchenpos.order.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuId {

    @Column(name = "menuId", nullable = false, updatable = false)
    private Long id;

    public MenuId(Long id) {
        this.id = id;
    }

    protected MenuId() {};

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuId menuId = (MenuId) o;
        return id == menuId.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
