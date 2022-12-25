package kitchenpos.menugroup.domain;

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

    protected MenuGroup() {}

    private MenuGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroup generate(String name) {
        return new MenuGroup(null, name);
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
