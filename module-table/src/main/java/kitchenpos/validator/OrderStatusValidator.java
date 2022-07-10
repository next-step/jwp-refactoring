package kitchenpos.validator;

import kitchenpos.domain.OrderTable;

import java.util.List;

public interface OrderStatusValidator {
    public void validateTableSeparate(List<OrderTable> orderTables);

    public void validateEnabledClear(final Long orderTableId);
}
