package kitchenpos.menu.domain;

import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {
    private static final int ZERO = 0;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts;

    protected MenuProducts() {}

   public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }
}
