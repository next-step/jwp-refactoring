package kitchenpos.ordertable.domain;

import kitchenpos.ordertable.event.TableCreateOrderEvent;
import kitchenpos.ordertable.exception.ClosedTableOrderException;
import kitchenpos.ordertable.exception.TableNotFoundException;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderTableValidateEventListener {

    private final OrderTableRepository orderTableRepository;

    public OrderTableValidateEventListener(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void validateNotOrderClosedTable(TableCreateOrderEvent orderCreateEvent) {
        Long orderTableId = orderCreateEvent.getOrderTableId();
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(TableNotFoundException::new);
        if (orderTable.isOrderClose()) {
            throw new ClosedTableOrderException();
        }
    }
}
