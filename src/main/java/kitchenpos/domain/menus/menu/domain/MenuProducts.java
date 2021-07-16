package kitchenpos.domain.menus.menu.domain;

import kitchenpos.domain.menus.Price;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Embeddable
class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected List<MenuProduct> list() {
        return Collections.unmodifiableList(menuProducts);
    }

    protected void validationByPrice(Price menuPrice, Map<Long, Price> productPriceMap) {
        Price totalPrice = calculateTotalPrice(productPriceMap);

        if (menuPrice.compareTo(totalPrice) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 `[메뉴의 수량] X [상품의 가격]` 보다 비쌀 수 없다.");
        }
    }

    private Price calculateTotalPrice(final Map<Long, Price> productPriceMap) {
        return this.menuProducts.stream()
            .map(menuProduct -> {
                Price productPrice = productPriceMap.get(menuProduct.getProductId());
                return menuProduct.calculate(productPrice);
            })
            .reduce(Price::plus)
            .orElse(Price.ZERO);
    }

    protected void add(final MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }
}
