package kitchenpos.ordertable.event;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.ordertable.validator.OrderTableValidator;
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
