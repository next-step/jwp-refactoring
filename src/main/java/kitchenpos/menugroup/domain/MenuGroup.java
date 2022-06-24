package kitchenpos.menugroup.domain;

import kitchenpos.core.domain.Name;

import javax.persistence.*;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;

    public MenuGroup(String name) {
        this.name = new Name(name);
    }

    protected MenuGroup() {
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }
}
