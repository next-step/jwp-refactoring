package kitchenpos.menu.domain;

import kitchenpos.common.domain.Name;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "menu_group")
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "bigint(20)")
    private Long id;
    @Embedded
    private Name name;

    public MenuGroup() {
    }

    public MenuGroup(String name) {
        this.name = new Name(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuGroup)) return false;
        MenuGroup menuGroup = (MenuGroup) o;
        return Objects.equals(getName(), menuGroup.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
