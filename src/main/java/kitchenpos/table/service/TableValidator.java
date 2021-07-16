package kitchenpos.table.service;

import java.util.List;

public interface TableValidator {
    void validateOrderStatusInCookingOrMeal(List<Long> orderTableIds);

    void validateOrderStatusInCookingOrMeal(Long orderTableId);
}
