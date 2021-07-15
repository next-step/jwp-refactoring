package kitchenpos.menuproduct.domain;

import kitchenpos.menuproduct.exception.NoMenuProductException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MenuProducts {
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
        validateSize();
    }

    public void add(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
    }

    public void addAll(List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
    }

    public List<MenuProduct> menuProducts() {
        return menuProducts;
    }

    public BigDecimal sumOfMenuProducts() {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.menuProductPrice());
        }

        return sum;
    }

    public void validateSize() {
        if(this.menuProducts.isEmpty()) {
            throw new NoMenuProductException();
        }
    }
}
