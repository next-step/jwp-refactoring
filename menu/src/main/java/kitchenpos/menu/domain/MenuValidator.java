package kitchenpos.menu.domain;

import java.util.List;

public interface MenuValidator {
    void validateHasProducts(List<Long> productIds);
    void validateMenuPrice(long menuPrice, List<Long> productIds);
}
