package kitchenpos.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static kitchenpos.common.ErrorMessage.INVALID_MENU_PRODUCT;
import static kitchenpos.domain.Price.ZERO_PRICE;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    private MenuProducts(List<MenuProduct> menuProducts) {
        validateMenuProductsIsEmpty(menuProducts);
        this.menuProducts = new ArrayList<>(menuProducts);
    }

    public static MenuProducts from(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    private void validateMenuProductsIsEmpty(List<MenuProduct> menuProducts) {
        if (menuProducts.isEmpty()) {
            throw new IllegalArgumentException(INVALID_MENU_PRODUCT.getMessage());
        }
    }

    public Price totalPrice() {
        Price totalPrice = ZERO_PRICE;
        for (MenuProduct menuProduct : menuProducts) {
            totalPrice = totalPrice.add(menuProduct.totalPrice());
        }
        return totalPrice;
    }

    public void setMenu(final Menu menu) {
        menuProducts.forEach(menuProduct -> menuProduct.setMenu(menu));
    }

    public List<MenuProduct> value() {
        return menuProducts;
    }
}
