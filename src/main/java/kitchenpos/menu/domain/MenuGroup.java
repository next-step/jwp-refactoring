package kitchenpos.menu.domain;

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
    @Embedded
    private MenuGroupName name;

    public MenuGroup() {
    }

    public MenuGroup(MenuGroupName name) {
        this.name = name;
    }

    public MenuGroup(Long id, MenuGroupName name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public MenuGroupName getName() {
        return name;
    }
}
