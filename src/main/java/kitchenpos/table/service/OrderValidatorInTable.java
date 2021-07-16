package kitchenpos.table.service;

import java.util.List;

public interface OrderValidatorInTable {
    void validateOrderStatusInCookingOrMeal(List<Long> orderTableIds);

    void validateOrderStatusInCookingOrMeal(Long orderTableId);
}
