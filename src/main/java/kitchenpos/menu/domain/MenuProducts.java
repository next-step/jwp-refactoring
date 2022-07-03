package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {
    private static final String ERROR_MESSAGE_EMPTY = "상품이 존재하지 않습니다.";
    private static final String ERROR_MESSAGE_INVALID_SUM = "상품 금액의 합이 메뉴 가격보다 작아야 합니다.";

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts, Menu menu, Price price) {
        validateEmpty(menuProducts);
        validateMenuPrice(menuProducts, price);
        setMenu(menuProducts, menu);
        this.menuProducts.addAll(menuProducts);
    }

    private void validateEmpty(List<MenuProduct> menuProducts) {
        if (menuProducts.isEmpty()) {
            throw new IllegalArgumentException(ERROR_MESSAGE_EMPTY);
        }
    }

    private void validateMenuPrice(List<MenuProduct> menuProducts, Price price) {
        Price sum = new Price(BigDecimal.ZERO);
        for (MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.getTotalPrice());
        }
        if (sum.isMoreExpensive(price)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_INVALID_SUM);
        }
    }

    private void setMenu(List<MenuProduct> menuProducts, Menu menu) {
        menuProducts.forEach(it -> it.setMenu(menu));
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
