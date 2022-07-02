package kitchenpos.menu.domain;

import kitchenpos.common.Name;
import kitchenpos.menu.dto.MenuGroupRequest;

import javax.persistence.*;

@Entity
public class MenuGroup {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;

    protected MenuGroup() {
    }

    public MenuGroup(String name) {
        this(null, new Name(name));
    }

    public MenuGroup(Long id, Name name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroup of(MenuGroupRequest request) {
        return new MenuGroup(request.getName());
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }
}
