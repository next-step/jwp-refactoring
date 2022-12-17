package kitchenpos.menu.domain;

import kitchenpos.exception.MenuErrorMessage;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Embeddable
public class MenuProducts {
    private static final int COMPARE_NUM = 0;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {}

    public void addMenuProduct(Menu menu, MenuProduct menuProduct) {
        if(!isContains(menuProduct)) {
            this.menuProducts.add(menuProduct);
            menuProduct.updateMenu(menu);
        }
    }

    private boolean isContains(MenuProduct menuProduct) {
        return this.menuProducts.contains(menuProduct);
    }

    public void validatePrice(BigDecimal price) {
        BigDecimal totalAmount = getTotalAmount();
        if(Objects.isNull(price) || isLessThanZero(price) || isMenuPriceGreaterThanTotalAmount(price, totalAmount)) {
            throw new IllegalArgumentException(MenuErrorMessage.INVALID_PRICE.getMessage());
        }
    }

    private BigDecimal getTotalAmount() {
        return menuProducts.stream()
                .map(MenuProduct::calculateAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean isLessThanZero(BigDecimal price) {
        return price.compareTo(BigDecimal.ZERO) < COMPARE_NUM;
    }

    private boolean isMenuPriceGreaterThanTotalAmount(BigDecimal price, BigDecimal totalAmount) {
        return price.compareTo(totalAmount) > COMPARE_NUM;
    }

    public List<MenuProduct> values() {
        return Collections.unmodifiableList(menuProducts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProducts that = (MenuProducts) o;
        return Objects.equals(menuProducts, that.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuProducts);
    }
}
