package kitchenpos.domain.menu;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.common.wrap.Name;

@Entity
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    protected MenuGroup() {

    }

    public MenuGroup(String name) {
        this.name = Name.from(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }
}
