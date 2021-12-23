package kitchenpos.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;

@Embeddable
public class MenuProducts {
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
        Price sumPrice = Price.from(0);
        for (final MenuProduct menuProduct : menuProducts) {    // TODO MenuProducts 내부로 이동
            sumPrice = sumPrice.add(menuProduct.getProduct()
                    .getPrice());
        }
        return sumPrice;
    }

    public void changeMenu(Menu menu) {
        menuProducts.forEach(menuProduct -> menuProduct.setMenu(menu));
    }

    public void add(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
    }
}
