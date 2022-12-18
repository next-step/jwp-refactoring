package kitchenpos.domain;

import kitchenpos.common.ErrorCode;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {
    private static final int ZERO = 0;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    @ReadOnlyProperty
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public MenuProducts() {}

    public void addMenuProduct(Menu menu, MenuProduct menuProduct) {
        if(hasMenuProduct(menuProduct)) {
            return;
        }
        this.menuProducts.add(menuProduct);
        menuProduct.updateMenu(menu);
    }

    private boolean hasMenuProduct(MenuProduct menuProduct) {
        return menuProducts.contains(menuProduct);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void validatePrice(BigDecimal price) {
        BigDecimal totalAmount = getTotalAmount();
        if(isMenuPriceGreaterThanTotalAmount(price, totalAmount)) {
            throw new IllegalArgumentException(ErrorCode.INVALID_ADD_MENU_PRICE.getErrorMessage());
        }
    }

    private boolean isMenuPriceGreaterThanTotalAmount(BigDecimal price, BigDecimal totalAmount) {
        return price.compareTo(totalAmount) > ZERO;
    }

    private BigDecimal getTotalAmount() {
        return menuProducts.stream()
                .map(MenuProduct::calculateAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
