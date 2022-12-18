package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public void add(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
    }

    public List<MenuProduct> value() {
        return Collections.unmodifiableList(menuProducts);
    }


}
