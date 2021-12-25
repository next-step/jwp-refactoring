package kitchenpos.menu.group.domain;

import javax.persistence.*;

@Table(name = "menu_group")
@Entity
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100, nullable = false, unique = true)
    private String name;

    protected MenuGroup() {

    }

    public MenuGroup(String name) {
        this.name = name;
    }

    public static MenuGroup create(String name) {
        return new MenuGroup(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
