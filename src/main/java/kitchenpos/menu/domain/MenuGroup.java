package kitchenpos.menu.domain;

import kitchenpos.product.domain.Name;

import javax.persistence.Column;
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
    @Column
    private Name name;

    private MenuGroup(Name name) {

        this.name = name;
    }

    public static MenuGroup from(Name name) {
        return new MenuGroup(name);
    }

    public MenuGroup() {
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
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
