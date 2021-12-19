package kitchenpos.menu.domain;

import kitchenpos.common.domain.Name;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    protected MenuGroup() {
    }

    private MenuGroup(Long id, Name name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroup of(Long id, Name name) {
        return new MenuGroup(id, name);
    }

    public static MenuGroup of(Name name) {
        return new MenuGroup(null, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }
}
