package kitchenpos.product.domain;

import kitchenpos.ExceptionMessage;
import kitchenpos.common.Price;
import kitchenpos.common.Quantity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MenuProducts {

    private List<MenuProduct> menuProducts;

    public MenuProducts() {
        this.menuProducts = new ArrayList<>();
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        if (menuProducts.isEmpty()) {
            throw new IllegalArgumentException(ExceptionMessage.EMPTY_MENU_PRODUCTS.getMessage());
        }
        this.menuProducts = menuProducts;
    }

    public void checkValidMenuPrice(Price price) {
        if (!price.lessOrEqualThan(getProductPriceSum())) {
            throw new IllegalArgumentException(ExceptionMessage.MENU_PRICE_LESS_PRODUCT_PRICE_SUM.getMessage());
        }
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

    public List<MenuProduct> getValue() {
        return menuProducts;
    }

}
