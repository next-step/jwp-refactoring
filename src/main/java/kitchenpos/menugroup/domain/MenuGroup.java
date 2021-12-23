package kitchenpos.menugroup.domain;

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

    private MenuGroup(Name name) {
        this.name = name;
    }

    private MenuGroup(Long id, Name name) {
        this(name);
        this.id = id;
    }

    public static MenuGroup of(String name) {
        return new MenuGroup(Name.of(name));
    }

    public static MenuGroup of(Long id, String name) {
        return new MenuGroup(id, Name.of(name));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }
}
