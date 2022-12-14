package kitchenpos.menu.domain;

import javax.persistence.*;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;

    public MenuGroup() {}

    public MenuGroup(Long id, String name) {
        this(name);
        this.id = id;
    }

    public MenuGroup(String name) {
        this.name = name;
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
