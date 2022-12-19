package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.Arrays;

public final class MenuRequestUtils {

    public static MenuRequest of(String name, BigDecimal price, Long productId, Long menuGroupId) {
        MenuProductRequest menuProductRequest = new MenuProductRequest(productId, 1L);
        return new MenuRequest(name, price, menuGroupId, Arrays.asList(menuProductRequest));
    }

    private MenuRequestUtils() {
    }
}
