package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.util.ObjectUtils;


@Entity
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    protected MenuGroup() {

    }

    private MenuGroup(String name) {
        validMenuGroupName(name);
        this.name = name;

    }

    public static MenuGroup of(String name) {
        return new MenuGroup(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    private void validMenuGroupName(String name) {
        if (ObjectUtils.isEmpty(name)) {
            throw new IllegalArgumentException("이름은 공백이면 안됩니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MenuGroup)) {
            return false;
        }
        MenuGroup menuGroup = (MenuGroup) o;
        return Objects.equals(getName(), menuGroup.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
