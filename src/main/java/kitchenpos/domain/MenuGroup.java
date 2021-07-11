package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Menus menus = new Menus();

    protected MenuGroup() {
    }

    public MenuGroup(String name) {
        this(null, name);
    }

    MenuGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void add(Menu menu) {
        menus.add(menu);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Menus getMenus() {
        return menus;
    }
}
