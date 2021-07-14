package kitchenpos.menu.domain;

import kitchenpos.common.Message;

import javax.persistence.*;

@Entity
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    private Menus menus = new Menus();

    protected MenuGroup() {
    }

    public MenuGroup(String name) {
        validateName(name);
        this.name = name;
    }

    public MenuGroup(Long id, String name) {
        validateName(name);
        this.id = id;
        this.name = name;
    }

    private void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException(Message.ERROR_MENUGROUP_NAME_REQUIRED.showText());
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
