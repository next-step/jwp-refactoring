package kitchenpos.menu.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "menu_group")
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private MenuGroupName name;

    private MenuGroup(MenuGroupName name) {

        this.name = name;
    }

    private MenuGroup(Long id, MenuGroupName name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroup from(MenuGroupName name) {
        return new MenuGroup(name);
    }

    public static MenuGroup from(Long id, String name) {
        return new MenuGroup(id, MenuGroupName.from(name));
    }

    public static MenuGroup from(String name) {
        return new MenuGroup(MenuGroupName.from(name));
    }

    protected MenuGroup() {
    }

    public Long getId() {
        return id;
    }

    public MenuGroupName getName() {
        return name;
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
        return Objects.equals(name, menuGroup.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
