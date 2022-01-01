package kitchenpos.menu.domain;

import kitchenpos.common.exception.IllegalArgumentException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"), nullable = false)
    private List<MenuProduct> menuProducts;

    protected MenuProducts() {}

    private MenuProducts(List<MenuProduct> menuProducts) {
        checkMenuProductIsEmpty(menuProducts);
        this.menuProducts = menuProducts;
    }

    private void checkMenuProductIsEmpty(List<MenuProduct> menuProducts) {
        if (menuProducts.isEmpty()) {
            throw new IllegalArgumentException("메뉴 상품 목록이 비어있습니다.");
        }
    }

    public static MenuProducts of(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    public BigDecimal calculateTotalPrice() {
        return menuProducts.stream()
                .map(MenuProduct::getMenuProductPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isEmpty() {
        return menuProducts.isEmpty();
    }

    public List<MenuProduct> asList() {
        return menuProducts;
    }
}
