package kitchenpos.menu.domain;

import static java.util.stream.Collectors.toList;

import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.common.Price;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menuId")
    private List<MenuProduct> menuProducts;

    protected MenuProducts() {
    }

    private MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public static MenuProducts of(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    public List<MenuProduct> getValues() {
        return menuProducts;
    }

    public List<Long> extractProductIds() {
        return getValues().stream()
            .map(MenuProduct::getProductId)
            .collect(toList());
    }

    public Price getTotalPrice() {
        Price totalPrice = Price.zero();
        for (MenuProduct menuProduct : this.menuProducts) {
            totalPrice = totalPrice.add(menuProduct.calculateProductsPrice());
        }
        return totalPrice;
    }
}
