package kitchenpos.menu.domain;

import kitchenpos.common.Name;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;

    protected MenuGroup() {
    }

    public MenuGroup(Name name) {
        validate(name);
        this.name = name;
    }

    public Long getId() {
        return id;
    }


    public String getName() {
        return this.name.getName();
    }

    @Override
    public String toString() {
        return "MenuGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    private void validate(Name name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException();
        }
    }
}
