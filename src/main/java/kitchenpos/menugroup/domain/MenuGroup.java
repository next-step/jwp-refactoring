package kitchenpos.menugroup.domain;

import kitchenpos.common.domain.BaseEntity;

import javax.persistence.*;

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
