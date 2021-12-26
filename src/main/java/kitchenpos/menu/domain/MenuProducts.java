package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.common.domain.Price;
import org.springframework.util.CollectionUtils;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    private MenuProducts(final List<MenuProduct> menuProducts, final Price menuPrice) {
        validateMenuProductsNotEmpty(menuProducts);
        validateNotOverPrice(menuProducts, menuPrice);

        this.menuProducts = menuProducts;
    }

    public static MenuProducts of(final List<MenuProduct> menuProducts, final Price menuPrice) {
        return new MenuProducts(menuProducts, menuPrice);
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }

    private void validateMenuProductsNotEmpty(final List<MenuProduct> menuProducts) {
        if (CollectionUtils.isEmpty(menuProducts)) {
            throw new IllegalArgumentException("메뉴 상품은 하나 이상여야 합니다.");
        }
    }

    private void validateNotOverPrice(final List<MenuProduct> menuProducts, final Price menuPrice) {
        BigDecimal totalPrice = getTotalPrice(menuProducts);

        if (menuPrice.isLessThan(totalPrice)) {
            throw new IllegalArgumentException("상품 가격의 총합이 메뉴의 가격을 초과할 수 없습니다.");
        }
    }

    private BigDecimal getTotalPrice(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
            .map(MenuProduct::getPrice)
            .reduce(BigDecimal::add)
            .orElse(BigDecimal.ZERO);
    }
}
