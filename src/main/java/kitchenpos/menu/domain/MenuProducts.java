package kitchenpos.menu.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class MenuProducts {
    private static final int ZERO = 0;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts;

    protected MenuProducts() {}

    public MenuProducts(List<MenuProduct> menuProducts) {
        validate(menuProducts);
        this.menuProducts = menuProducts;
    }

    private void validate(List<MenuProduct> menuProducts) {

    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }
}
