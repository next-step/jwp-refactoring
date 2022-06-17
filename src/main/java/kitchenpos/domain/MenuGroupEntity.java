package kitchenpos.domain;

import javax.persistence.*;

@Entity(name = "menu_group")
public class MenuGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;

    public MenuGroupEntity(String name) {
        this.name = new Name(name);
    }

    protected MenuGroupEntity() {
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }
}
