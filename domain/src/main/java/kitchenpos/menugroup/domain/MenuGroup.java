package kitchenpos.menugroup.domain;

import kitchenpos.common.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class MenuGroup extends BaseEntity {
    @Column
    private String name;

    public MenuGroup(String name) {
        this.name = name;
    }

    protected MenuGroup() {
    }

    public String getName() {
        return name;
    }
}
