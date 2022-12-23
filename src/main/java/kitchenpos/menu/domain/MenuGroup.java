package kitchenpos.menu.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class MenuGroup {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    protected MenuGroup() {}

    public MenuGroup(long id, String name) {
        this.id = id;
        this.name = name;
    }

    private MenuGroup(String name) {
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
