package kitchenpos.menu.domain;

import kitchenpos.product.domain.Price;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts;

    protected MenuProducts() {
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
