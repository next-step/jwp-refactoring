package kitchenpos.menugroup.domain;


import javax.persistence.*;

import static java.util.Objects.requireNonNull;

@Entity
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public MenuGroup() {
    }

    public MenuGroup(String name) {
        requireNonNull(name, "name");
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
