package kitchenpos.menu.domain;

import kitchenpos.domain.Name;

import javax.persistence.*;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    public MenuGroup() {
    }

    private MenuGroup(Long id, String name) {
        this.id = id;
        this.name = Name.of(name);
    }

    public static MenuGroup of(Long id, String name) {
        return new MenuGroup(id, name);
    }

    public static MenuGroup of(String name) {
        return new MenuGroup(null, name);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name.getName();
    }

    public void setName(final String name) {
        this.name = Name.of(name);
    }
}
