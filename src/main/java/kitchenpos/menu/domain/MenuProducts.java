package kitchenpos.menu.domain;

import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    List<MenuProduct> menuProducts = new ArrayList<>();

    public MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void addMenuProducts(Menu menu, List<MenuProduct> menuProducts) {
        for (MenuProduct menuProduct : menuProducts) {
            this.menuProducts.add(new MenuProduct(menu, menuProduct.getProduct(), menuProduct.getQuantity()));
        }
    }

    public Price getTotalPrice() {
        Price sum = Price.of(BigDecimal.ZERO);
        for (final MenuProduct menuProduct : this.menuProducts) {
            final Product product = menuProduct.getProduct();

            sum = sum.add(product.getPrice().multiply(menuProduct.getQuantity()));
        }
        return sum;
    }
}
