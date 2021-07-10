package kitchenpos.domain.menugroup;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class MenuGroup implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    // for jpa
    public MenuGroup() {
    }

    private MenuGroup(Long id, Name name) {
        this.id = id;
        this.name = name;
    }

    @Deprecated
    public static MenuGroup of(String name) {
        return new MenuGroup(null, Name.of(name));
    }

    public static MenuGroup of(Name name) {
        return new MenuGroup(null, name);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    @Override
    public String toString() {
        return "MenuGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
