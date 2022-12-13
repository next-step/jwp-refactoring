package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public List<MenuProduct> getMenuProducts() {
        return this.menuProducts;
    }

    public void addAll(List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
    }
}
