package kitchenpos.menu.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "menu_group")
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private MenuName name;

    protected MenuGroup() {}

    private MenuGroup(Long id, String name) {
        this.id = id;
        this.name = MenuName.from(name);
    }

    private MenuGroup(String name) {
        this.name = MenuName.from(name);
    }

    public static MenuGroup of(Long id, String name) {
        return new MenuGroup(id, name);
    }

    public static MenuGroup from(String name) {
        return new MenuGroup(name);
    }

    public Long getId() {
        return id;
    }

    public String findName() {
        return name.getValue();
    }
}
