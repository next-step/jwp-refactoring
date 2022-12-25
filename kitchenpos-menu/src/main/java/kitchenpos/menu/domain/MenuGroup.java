package kitchenpos.menu.domain;

import kitchenpos.common.ErrorMessage;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    protected MenuGroup() {}

    public MenuGroup(String name) {
        validate(name);
        this.name = name;
    }

    private void validate(String name) {
        if(Objects.isNull(name) || name.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.MENU_GROUP_REQUIRED_NAME.getMessage());
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuGroup menuGroup = (MenuGroup) o;
        return Objects.equals(name, menuGroup.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
