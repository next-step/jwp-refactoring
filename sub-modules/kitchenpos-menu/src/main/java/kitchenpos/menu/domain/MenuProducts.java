package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menuId", cascade = CascadeType.ALL)
    private List<MenuProduct> values;

    protected MenuProducts() {
        values = new ArrayList<>();
    }

    public void add(MenuProduct menuProduct) {
        values.add(menuProduct);
    }

    public List<MenuProduct> values() {
        return Collections.unmodifiableList(values);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuProducts)) return false;
        MenuProducts that = (MenuProducts) o;
        return Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }
}
