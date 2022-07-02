package kitchenpos.menugroup.domain;

import javax.persistence.*;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;

    protected MenuGroup() {
    }

    public MenuGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    private MenuGroup(String name) {
        this.name = name;
    }

    public static MenuGroup from(String name) {
        return new MenuGroup(name);
    }

    public static MenuGroup of(Long id, String name) {
        return new MenuGroup(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
