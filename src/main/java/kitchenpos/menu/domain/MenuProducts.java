package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

public class MenuProducts {
    private static final int COMPARE_NUM = 0;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    protected MenuProducts(List<MenuProduct> menuProducts){
        this.menuProducts.addAll(menuProducts);
    }
}
