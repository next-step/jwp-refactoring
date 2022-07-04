package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<MenuProduct> menuProducts;

    public MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void validateTotalPriceNotExpensiveThanEach(Price menuPrice) {
        Price sum = new Price(BigDecimal.ZERO);
        for (MenuProduct menuProduct : menuProducts) {
            if (menuProduct.getProduct() == null) {
                throw new IllegalArgumentException();
            }
            Product product = menuProduct.getProduct();
            sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (menuPrice.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }
}
