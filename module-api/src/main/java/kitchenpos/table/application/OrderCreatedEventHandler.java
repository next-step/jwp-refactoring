package kitchenpos.table.application;

import kitchenpos.order.domain.OrderCreatedEvent;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
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
