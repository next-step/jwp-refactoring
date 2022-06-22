package kitchenpos.order.domain;

import kitchenpos.core.domain.DomainService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableEmptyChangedEvent;
import kitchenpos.table.exception.CannotChangeEmptyException;
import kitchenpos.table.exception.NotFoundOrderTableException;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

@DomainService
public class TableEmptyChangedEventHandler {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableEmptyChangedEventHandler(OrderTableRepository orderTableRepository,
                                         OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    @Transactional
    public void handle(TableEmptyChangedEvent event) {
        OrderTable orderTable = findById(event.getOrderTableId());
        if (hasUncompletedOrder(orderTable.getId())) {
            throw new CannotChangeEmptyException();
        }
        if (orderTable.isGrouped()) {
            throw new CannotChangeEmptyException();
        }
    }

    private boolean hasUncompletedOrder(Long orderTableId) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, OrderStatus.getUncompletedStatuses());
    }

    public OrderTable findById(Long id) {
        return orderTableRepository.findById(id)
                                   .orElseThrow(NotFoundOrderTableException::new);
    }
}
