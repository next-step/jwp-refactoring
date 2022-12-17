package kitchenpos.table.event;

import kitchenpos.order.event.OrderCreateEvent;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderTableEventHandler {

    private final OrderTableRepository orderTableRepository;

    public OrderTableEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    public void handle(OrderCreateEvent event) {
        orderTableRepository.findById(event.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 없습니다."));
    }

}
