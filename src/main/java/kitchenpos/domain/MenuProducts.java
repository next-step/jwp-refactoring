package kitchenpos.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> elements = new ArrayList<>();

    public List<MenuProduct> getElements() {
        return elements;
    }

    public void addAll(final Menu menu, final List<MenuProduct> menuProducts) {
        if (menu.hasPriceGreaterThan(totalPriceOf(menuProducts))) {
            throw new IllegalArgumentException("메뉴 가격은 구성 상품 금액 총합보다 크면 안 됩니다.");
        }

        for (MenuProduct menuProduct : menuProducts) {
            elements.add(menuProduct);
            menuProduct.setMenu(menu);
        }
    }

    private BigDecimal totalPriceOf(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(menuProduct -> menuProduct.getPrice())
                .reduce(BigDecimal.ZERO, (acc, price) -> acc.add(price));
    }
}
