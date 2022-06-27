package kitchenpos.table.application.handler;

import kitchenpos.order.domain.event.OrderTableEmptyCheckEvent;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.exception.OrderTableAlreadyEmptyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Transactional(readOnly = true)
@Component
public class OrderTableEmptyCheckEventHandler {

    private final OrderTableRepository orderTableRepository;

    public OrderTableEmptyCheckEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @TransactionalEventListener
    @Transactional
    public void handle(OrderTableEmptyCheckEvent event) {
        OrderTable orderTable = orderTableRepository.findById(event.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new OrderTableAlreadyEmptyException(orderTable.getId());
        }
    }
}
