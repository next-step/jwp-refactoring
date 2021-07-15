package kitchenpos.menu.domain;

import kitchenpos.common.model.Price;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public List<MenuProduct> list() {
        return Collections.unmodifiableList(menuProducts);
    }

    public void validationByPrice(Price menuPrice, Map<Long, Price> productPriceMap) {
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
            .orElse(Price.ZERO());
    }

    public void add(final MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }
}
