package kitchenpos.menu.domain;

import javax.persistence.*;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private MenuGroupName name;

    public MenuGroup() {
    }

    public MenuGroup(Long id, String name) {
        this.id = id;
        this.name = new MenuGroupName(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }
}
