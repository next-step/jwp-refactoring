package kitchenpos.menu.application;

import java.math.BigDecimal;

public interface ProductMenuService {
    BigDecimal calculateProductsPrice(Long productId, Long productCount);
}
