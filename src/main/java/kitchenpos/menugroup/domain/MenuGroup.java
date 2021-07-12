package kitchenpos.menugroup.domain;

import javax.persistence.*;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private MenuGroupName menuGroupName;

    public MenuGroup() {
    }

    public MenuGroup(String name) {
        this.menuGroupName = new MenuGroupName(name);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public MenuGroupName getMenuGroupName() {
        return menuGroupName;
    }
}
