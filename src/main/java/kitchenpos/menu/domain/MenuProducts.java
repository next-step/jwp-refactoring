package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts;

    protected MenuProducts() {
    }

    private MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public static MenuProducts of(Menu menu, List<MenuProduct> menuProducts) {
        menuProducts.forEach(menuProduct -> menuProduct.bindMenu(menu));
        return new MenuProducts(menuProducts);
    }

    public List<MenuProduct> getValues() {
        return menuProducts;
    }

    public List<Long> getProductIds() {
        return getValues().stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
    }

    public Price getTotalPrice() {
        Price totalPrice = Price.of(0);
        for (MenuProduct menuProduct : menuProducts) {
            totalPrice = totalPrice.add(menuProduct.calculateProductsPrice());
        }
        return totalPrice;
    }
}
