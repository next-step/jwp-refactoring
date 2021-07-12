package kitchenpos.menu.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class MenuGroup {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String name;

    protected MenuGroup() {
    }

    public MenuGroup(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
