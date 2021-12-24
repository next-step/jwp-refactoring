package kitchenpos.application.table;

import kitchenpos.domain.order.OrderCreatedEvent;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.repository.table.OrderTableRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
public class OrderCreatedEventHandler {

    private final OrderTableRepository orderTableRepository;

    public OrderCreatedEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    public void checkOrderTableIsExistAndNotEmpty(OrderCreatedEvent event) {

        final OrderTable orderTable = orderTableRepository.findById(event.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("등록된 주문테이블이 아닙니다."));

        orderTable.checkIsEmpty();

    }

}
