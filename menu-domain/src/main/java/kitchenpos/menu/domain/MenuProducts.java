package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;

@Embeddable
public class MenuProducts {

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "menu", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public Price getMenuProductsSum() {
        Price sum = new Price(BigDecimal.ZERO);
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = menuProduct.getProduct();
            sum = sum.add(product.getPrice().multiply(menuProduct.getQuantity()));
        }
        return sum;
    }

    public void add(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }

    public void groupToMenu(Menu menu) {
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.groupToMenu(menu);
        }
    }

    public boolean priceSumIsLessThan(Price price) {
        return getMenuProductsSum().isLessThan(price);
    }

}
