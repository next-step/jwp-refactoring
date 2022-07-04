package kitchenpos.menu.domain;

import kitchenpos.global.Name;

import javax.persistence.*;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    public MenuGroup() {
    }

    private MenuGroup(String name) {
        this.name = new Name(name);
    }

    public static MenuGroup of(String name) {
        return new MenuGroup(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }
}
