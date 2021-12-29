package kitchenpos.menus.menugroup.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.domain.Name;

@Entity
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    protected MenuGroup() {
    }

    public MenuGroup(final Long id, final Name name) {
        this.id = id;
        this.name = name;
    }

    public MenuGroup(final Name name) {
        this(null, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.asString();
    }
}
