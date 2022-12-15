package kitchenpos.domain;

import static javax.persistence.GenerationType.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import kitchenpos.dto.MenuGroupRequest;

@Entity
public class MenuGroup {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;
    private String name;

    public MenuGroup() {
    }

    public MenuGroup(String name) {
        this.name = name;
    }

    public static MenuGroup of(MenuGroupRequest request) {
        return new MenuGroup(request.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
