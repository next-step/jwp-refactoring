package kitchenpos.domain.menuGroup;

import kitchenpos.dto.menuGroup.MenuGroupRequest;

import javax.persistence.*;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public MenuGroup() {
    }

    public MenuGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroup of(MenuGroupRequest menuGroupRequest) {
        return new MenuGroup(menuGroupRequest.getId(), menuGroupRequest.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
