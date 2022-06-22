package kitchenpos.menu.domain;

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

    protected MenuGroup() {
    }

    private MenuGroup(Long id) {
        this.id = id;
    }

    private MenuGroup(String name) {
        this.name = name;
    }

    public static MenuGroup of(Long id) {
        return new MenuGroup(id);
    }

    public static MenuGroup of(String name) {
        return new MenuGroup(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
