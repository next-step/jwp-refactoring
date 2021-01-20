package kitchenpos.menugroup.domain;

import javax.persistence.*;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;

    public MenuGroup(String name) {
        this.name = name;
    }

    protected MenuGroup() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
