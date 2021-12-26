package kitchenpos.menu.domain;

import kitchenpos.product.domain.Price;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;

@Embeddable
public class MenuProducts {
    public static final int INITAL_SUM_PRICE = 0;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    List<MenuProduct> menuProducts;

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public MenuProducts() {

    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public Price getSumPrice() {
        Price sumPrice = Price.from(INITAL_SUM_PRICE);
        for (final MenuProduct menuProduct : menuProducts) {
            sumPrice = sumPrice.add(menuProduct.getPrice());
        }
        return sumPrice;
    }

    public void changeMenu(Menu menu) {
        menuProducts.forEach(menuProduct -> menuProduct.changeMenu(menu));
    }

    public void add(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
    }
}
