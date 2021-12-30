package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;

public interface MenuValidator {
    void validateCreateMenu(Long menuGroupId, BigDecimal price, List<MenuProduct> menuProducts);
}
