package kitchenpos.orderTable.event;

import java.util.List;
import kitchenpos.orderTable.domain.OrderTableRepository;
import kitchenpos.orderTable.domain.OrderTables;
import kitchenpos.orderTable.validator.OrderTableValidator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Transactional(readOnly = true)
@Component
public class ReserveEventHandler {
    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public ReserveEventHandler(OrderTableRepository orderTableRepository, OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @TransactionalEventListener
    @Transactional
    public void reserveEvent(ReserveEvent event) {
        List<Long> orderTableIds = event.getOrderTableIds();
        final OrderTables savedOrderTables = OrderTables.from(orderTableRepository.findAllByIdIn(orderTableIds));
        orderTableValidator.validateReserveEvent(savedOrderTables, orderTableIds);
        savedOrderTables.reserve(event.getTableGroupId());
    }
}
