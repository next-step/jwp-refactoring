package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
}
