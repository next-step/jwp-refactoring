package kitchenpos.menugroup.domain;

import kitchenpos.common.domain.Name;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "menu_group")
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    protected MenuGroup() {
    }

    private MenuGroup(Long id, Name name) {
        this(name);
        this.id = id;
    }

    private MenuGroup(Name name) {
        this.name = name;
    }

    public static MenuGroup of(Long id, Name name) {
        return new MenuGroup(id, name);
    }

    public static MenuGroup of(Name name) {
        return new MenuGroup(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuGroup menuGroup = (MenuGroup) o;
        return Objects.equals(getName(), menuGroup.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
