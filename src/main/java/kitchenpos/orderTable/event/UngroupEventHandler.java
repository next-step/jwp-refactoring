package kitchenpos.orderTable.event;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.orderTable.domain.OrderTable;
import kitchenpos.orderTable.domain.OrderTableRepository;
import kitchenpos.orderTable.validator.OrderTableValidator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Transactional(readOnly = true)
@Component
public class UngroupEventHandler {
    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public UngroupEventHandler(OrderTableRepository orderTableRepository, OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @TransactionalEventListener
    @Transactional
    public void ungroupEvent(UngroupEvent event) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(event.getTableGroupId());
        orderTableValidator.validateComplete(orderTables.stream()
                .map(OrderTable::id)
                .collect(Collectors.toList()));
        orderTables.forEach(OrderTable::unGroup);
    }
}
