package kitchenpos.product.domain;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.domain.Name;
import org.springframework.util.Assert;

@Entity
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Embedded
    private Name name;

    protected MenuGroup() {
    }

    private MenuGroup(Name name) {
        Assert.notNull(name, "이름은 필수입니다.");
        this.name = name;
    }

    public static MenuGroup from(Name name) {
        return new MenuGroup(name);
    }

    public long id() {
        return id;
    }

    public Name name() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
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
        return Objects.equals(id, menuGroup.id) && Objects
            .equals(name, menuGroup.name);
    }

    @Override
    public String toString() {
        return "MenuGroup{" +
            "id=" + id +
            ", name=" + name +
            '}';
    }
}
