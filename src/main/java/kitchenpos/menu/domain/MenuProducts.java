package kitchenpos.menu.domain;

import kitchenpos.product.domain.Price;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menuId")
    private List<MenuProduct> menuProducts;

    public MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public Price getProductTotalPrice() {
        return menuProducts.stream()
                .map(menuProduct -> menuProduct.getProduct()
                        .getPrice().multiply(menuProduct.getQuantity()))
                .reduce(new Price(BigDecimal.ZERO), Price::add)
                ;
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }
}
