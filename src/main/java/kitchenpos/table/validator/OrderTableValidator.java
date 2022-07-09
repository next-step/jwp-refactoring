package kitchenpos.table.validator;

import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.exception.TableException;
import kitchenpos.table.exception.TableExceptionType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderTableValidator {
    private final OrderRepository orderRepository;

    public OrderTableValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateTableSeparate(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            validateEnabledClear(orderTable.getId());
        }
    }

    public void validateEnabledClear(final Long orderTableId) {
        orderRepository.findByOrderTableId(orderTableId).ifPresent(it -> {
            if (!it.getOrderStatus().enabledTableClear()) {
                throw new TableException(TableExceptionType.IMPOSSIBLE_ORDER_STATUS);
            }
        });
    }
}
