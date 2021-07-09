package kitchenpos.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class MenuGroup {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "menuGroup")
    private List<Menu> menus = new ArrayList<>();

    protected MenuGroup() {
    }

    public MenuGroup(Long id, String name) {
        this(name);
        this.id = id;
    }

    public void addMenu(Menu menu) {
        if (!menus.contains(menu)) {
            menus.add(menu);
        }
    }

    public MenuGroup(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
