package kitchenpos.menu.domain;

import kitchenpos.menu.exception.MenuExceptionCode;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MenuProducts {
    private static final int COMPARE_NUM = 0;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {}

    public void addMenuProduct(Menu menu, MenuProduct menuProduct) {
        if(!hasMenuProduct(menuProduct)) {
            this.menuProducts.add(menuProduct);
            menuProduct.updateMenu(menu);
        }
    }

    private boolean hasMenuProduct(MenuProduct menuProduct) {
        return this.menuProducts.contains(menuProduct);
    }

    public void validatePrice(BigDecimal price) {
        BigDecimal totalAmount = getTotalAmount();
        if(isMenuPriceGreaterThanTotalAmount(price, totalAmount)) {
            throw new IllegalArgumentException(MenuExceptionCode.INVALID_PRICE.getMessage());
        }
    }

    private BigDecimal getTotalAmount() {
        //BigDecimal totalAmount = BigDecimal.ZERO;
        return menuProducts.stream()
                .map(MenuProduct::calculateAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //return totalAmount;
    }

    private boolean isMenuPriceGreaterThanTotalAmount(BigDecimal price, BigDecimal totalAmount) {
        return price.compareTo(totalAmount) > COMPARE_NUM;
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }
}
