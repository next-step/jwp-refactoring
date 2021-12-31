package kitchenpos.core.menugroup.domain;

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
    private MenuGroupName name;

    protected MenuGroup() {
    }

    private MenuGroup(String name) {
        this.name = MenuGroupName.of(name);
    }

    private MenuGroup(long id, String name) {
        this.id = id;
        this.name = MenuGroupName.of(name);
    }

    public static MenuGroup of(String name) {
        return new MenuGroup(name);
    }

    public static MenuGroup generate(long id, String name) {
        return new MenuGroup(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public boolean matchName(String targetName) {
        return this.name.matchName(targetName);
    }
}
