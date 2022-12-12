package kitchenpos.menugroup.domain;

import kitchenpos.common.domain.Name;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;

    protected MenuGroup() {}

    public MenuGroup(Long id, Name name) {
        this.id = id;
        this.name = name;
    }

    public MenuGroup(Name name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuGroup menuGroup = (MenuGroup) o;
        return Objects.equals(id, menuGroup.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
