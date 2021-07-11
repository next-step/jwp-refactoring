package kitchenpos.menu.domain;

import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private final Menus menus = new Menus();

    public MenuGroup() {
    }

    public MenuGroup(final String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addMenu(final Menu menu) {
        menus.add(menu);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final MenuGroup menuGroup = (MenuGroup)o;
        return Objects.equals(id, menuGroup.id) && Objects.equals(name, menuGroup.name)
            && Objects.equals(menus, menuGroup.menus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, menus);
    }
}
