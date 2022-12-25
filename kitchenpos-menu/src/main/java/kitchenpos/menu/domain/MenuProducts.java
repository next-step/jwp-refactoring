package kitchenpos.menu.domain;

import kitchenpos.common.ErrorMessage;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MenuProduct> menuProducts = new HashSet<>();

    protected MenuProducts() {}

    public void addMenuProduct(Menu menu, MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
        menuProduct.updateMenu(menu);
    }

    public void validatePrice(BigDecimal price) {
        BigDecimal totalAmount = getTotalAmount();
        if(Objects.isNull(price) || isLessThanZero(price) || isMenuPriceGreaterThanTotalAmount(price, totalAmount)) {
            throw new IllegalArgumentException(ErrorMessage.MENU_INVALID_PRICE.getMessage());
        }
    }

    private BigDecimal getTotalAmount() {
        return menuProducts.stream()
                .map(MenuProduct::calculateAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean isLessThanZero(BigDecimal price) {
        return price.compareTo(BigDecimal.ZERO) < 0;
    }

    private boolean isMenuPriceGreaterThanTotalAmount(BigDecimal price, BigDecimal totalAmount) {
        return price.compareTo(totalAmount) > 0;
    }

    public Set<MenuProduct> values() {
        return Collections.unmodifiableSet(menuProducts);
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
