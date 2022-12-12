package kitchenpos.menu.domain;

import javax.persistence.*;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;

    protected MenuGroup() {
    }

    private MenuGroup(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroup of(long id, String name) {
        return new MenuGroup(id,name);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
