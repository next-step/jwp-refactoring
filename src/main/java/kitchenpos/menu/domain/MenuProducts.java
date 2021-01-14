package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class MenuProducts {
    private static final int LESS_THAN_ZERO = 0;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id")
    private final List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    protected MenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
    }

    public static MenuProducts of(final List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    public void validateTotalPrice() {
        final BigDecimal totalPrice = this.getAllMenuProductsPrice();

        if (BigDecimal.ZERO.compareTo(totalPrice) >= LESS_THAN_ZERO) {
            throw new IllegalArgumentException("상품 금액 또는 수량이 유효하지 않습니다.");
        }
    }

    public BigDecimal getAllMenuProductsPrice() {
        BigDecimal totalPrice = new BigDecimal(BigInteger.ZERO);

        for (MenuProduct menuProduct : menuProducts) {
            final BigDecimal tempPrice = menuProduct.getTotalPrice();
            totalPrice = totalPrice.add(tempPrice);
        }

        return totalPrice;
    }

    public void linkMenu(final Menu menu) {
        this.menuProducts.forEach(menuProduct -> menuProduct.linkMenu(menu));
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }
}
