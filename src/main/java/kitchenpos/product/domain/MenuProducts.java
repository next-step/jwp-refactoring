package kitchenpos.product.domain;

import kitchenpos.ExceptionMessage;
import kitchenpos.menu.domain.Quantity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MenuProducts {

    private List<MenuProduct> menuProducts;

    public MenuProducts() {
        if (menuProducts.isEmpty()) {
            throw new IllegalArgumentException(ExceptionMessage.EMPTY_MENU_PRODUCTS.getMessage());
        }
        this.menuProducts = new ArrayList<>();
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public Price getProductPriceSum() {
        Price totalPrice = new Price(BigDecimal.ZERO);
        for (MenuProduct menuProduct : menuProducts) {
            Price price = menuProduct.getProduct()
                    .getPrice();
            Quantity quantity = menuProduct.getQuantity();
            totalPrice = totalPrice.add(
                    price.multiplyQuantity(quantity));
        }
        return totalPrice;
    }

    public boolean isEmpty() {
        return menuProducts.isEmpty();
    }

    public List<MenuProduct> getValue() {
        return menuProducts;
    }
}
