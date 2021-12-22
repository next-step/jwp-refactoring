package kitchenpos.menu.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    public static MenuGroup of(Long id, String name) {
        return new MenuGroup(id, name);
    }

    public MenuGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    protected MenuGroup(){}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
