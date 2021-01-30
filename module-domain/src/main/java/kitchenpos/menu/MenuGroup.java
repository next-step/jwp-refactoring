package kitchenpos.menu;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "menuGroup")
    List<Menu> menu = new ArrayList<>();

    protected MenuGroup() {
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
