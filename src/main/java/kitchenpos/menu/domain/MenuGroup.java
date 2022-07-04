package kitchenpos.menu.domain;

import kitchenpos.common.domain.Name;

import javax.persistence.*;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;

    protected MenuGroup() {
    }

    public MenuGroup(String name) {
        this.name = new Name(name);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }
}
