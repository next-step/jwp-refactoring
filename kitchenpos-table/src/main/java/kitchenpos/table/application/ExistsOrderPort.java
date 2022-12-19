package kitchenpos.table.application;

import java.util.List;

public interface ExistsOrderPort {

    boolean existsOrderStatusCookingOrMeal(Long orderTableId);

    boolean existsOrderStatusCookingOrMeal(List<Long> orderTableIds);
}
