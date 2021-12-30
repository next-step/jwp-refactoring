package kitchenpos.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "name", column = @Column(name = "name", nullable = false))
    private Name name;

    public MenuGroup() {
    }

    public MenuGroup(final Long id, final String name) {
        this.id = id;
        this.name = Name.from(name);
    }

    public static MenuGroup of(final Long id, final String name) {
        return new MenuGroup(id, name);
    }

    public static MenuGroup of(final String name) {
        return new MenuGroup(null, name);
    }

    public static MenuGroup from(final String name) {
        return new MenuGroup(null, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.toName();
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
