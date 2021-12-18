package kitchenpos.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "menuGroup", cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Menu> menus = new ArrayList<>();

    public MenuGroup() {
    }

    public MenuGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public MenuGroup(String name) {
        this.name = name;
    }

    public Menu createMenu(String name, Long price) {
        Menu menu = new Menu(name, price, this);
        menus.add(menu);
        return menu;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuGroup menuGroup = (MenuGroup) o;
        return id.equals(menuGroup.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
