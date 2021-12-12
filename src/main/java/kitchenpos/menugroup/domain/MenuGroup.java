package kitchenpos.menugroup.domain;

import javax.persistence.Column;
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
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    protected MenuGroup() {}

    private MenuGroup(Long id) {
        this.id = id;
    }

    private MenuGroup(Long id, String name) {
        this(name);
        this.id = id;
    }

    private MenuGroup(String name) {
        this.name = name;
    }

    public static MenuGroup from(Long id) {
        return new MenuGroup(id);
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
}
