package kitchenpos.domain;

import kitchenpos.common.ErrorCode;

import javax.persistence.Column;
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

    @Column(length = 255, nullable = false, unique = true)
    private String name;

    public MenuGroup() {}

    public MenuGroup(String name) {
        validation(name);
        this.name = name;
    }

    private void validation(String name) {
        if (Objects.isNull(name) || name.isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.INVALID_FORMAT_MENU_GROUP_NAME.getErrorMessage());
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
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MenuGroup menuGroup = (MenuGroup) o;
        return Objects.equals(name, menuGroup.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public void setId(long id) {
    }

    public void setName(String name) {
    }
}
