package menu.domain;

import common.domain.Name;

import javax.persistence.*;

@Entity
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;

    protected MenuGroup() {
    }

    public MenuGroup(Long id, String name) {
        this.id = id;
        this.name = new Name(name);
    }

    public MenuGroup(String name) {
        this(null, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }
}
