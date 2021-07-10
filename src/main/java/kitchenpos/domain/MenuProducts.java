package kitchenpos.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

public class MenuProducts {
    private final List<MenuProduct> menuProducts;

    public MenuProducts(List<MenuProduct> menuProducts) {
        validate(menuProducts);
        this.menuProducts = menuProducts;
    }

    private void validate(List<MenuProduct> menuProducts) {
        if (CollectionUtils.isEmpty(menuProducts)) {
            throw new IllegalArgumentException();
        }
    }

    public List<Long> ids() {
        return menuProducts.stream()
            .map(MenuProduct::getProductId)
            .collect(Collectors.toList());
    }

    public List<MenuProduct> toList() {
        return menuProducts;
    }
}
