package kitchenpos.tablegroup.application;

import java.util.List;

public interface TableGroupOrderValidator {
    void validateExistsOrdersStatusIsCookingOrMeal(List<Long> orderTableIds);
}
