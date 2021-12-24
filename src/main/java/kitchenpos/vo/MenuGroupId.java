package kitchenpos.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.domain.menugroup.MenuGroup;

@Embeddable
public class MenuGroupId {
    @Column(name = "menu_group_id")
    private final Long menuGroupId;

    protected MenuGroupId() {
        this.menuGroupId = null;
    }

    private MenuGroupId(Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public static MenuGroupId of(Long menuGroupId) {
        return new MenuGroupId(menuGroupId);
    }

    public static MenuGroupId of(MenuGroup menuGroup) {
        return new MenuGroupId(menuGroup.getId());
    }

    public Long value() {
        return this.menuGroupId;
    }
}
