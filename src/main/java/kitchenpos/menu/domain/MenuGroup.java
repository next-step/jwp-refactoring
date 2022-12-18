package kitchenpos.menu.domain;

import static javax.persistence.GenerationType.*;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import kitchenpos.product.domain.Name;

@Entity
public class MenuGroup {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Embedded
    private Name name;

    public MenuGroup() {
    }

    public MenuGroup(Name name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }
}
