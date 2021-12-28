package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuGroupId {
    @Column(name = "menu_group_id")
    private final Long id;

    protected MenuGroupId() {
        this.id = null;
    }

    private MenuGroupId(Long id) {
        this.id = id;
    }

    public static MenuGroupId of(Long id) {
        return new MenuGroupId(id);
    }

    public Long value() {
        return this.id;
    }
}
