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

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
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
            checkIsNotNull(menuProduct);

            Product product = menuProduct.getProduct();
            sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        checkPriceNotExpensiveThanSum(menuPrice, sum);
    }

    private void checkIsNotNull(MenuProduct menuProduct) {
        if (menuProduct.getProduct() == null) {
            throw new IllegalArgumentException();
        }
    }

    private void checkPriceNotExpensiveThanSum(Price menuPrice, Price sum) {
        if (menuPrice.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }
}
