package kitchenpos.domain.menu;

import kitchenpos.domain.Name;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Name name;

    protected MenuGroup() {
    }

    public MenuGroup(Name name) {
        this(null, name);
    }

    public MenuGroup(Long id, Name name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public Name getName() { return name; }
}
