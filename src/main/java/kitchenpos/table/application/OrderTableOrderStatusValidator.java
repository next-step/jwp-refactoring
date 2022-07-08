package kitchenpos.table.application;

import java.util.List;

public interface OrderTableOrderStatusValidator {

    void validateOrderStatus(List<Long> orderTableIds);
}
