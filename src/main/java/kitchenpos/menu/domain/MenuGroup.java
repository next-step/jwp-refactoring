package kitchenpos.menu.domain;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.Name;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;

    protected MenuGroup() {
    }

    private MenuGroup(Long id, String name) {
        this.id = id;
        this.name = Name.of(name);
    }

    public static MenuGroup of(Long id, String name) {
        return new MenuGroup(id, name);
    }

    public Long id() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Name name() {
        return name;
    }

    public void setName(final Name name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuGroup menuGroup = (MenuGroup) o;
        return Objects.equals(id, menuGroup.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
