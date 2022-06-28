package kitchenpos.application.table;

import kitchenpos.domain.order.OrderTableValidateEvent;
import kitchenpos.domain.table.OrderTableRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TableEventHandler {

    private final OrderTableRepository orderTableRepository;

    public TableEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener(OrderTableValidateEvent.class)
    public void validateOrderTable(OrderTableValidateEvent event) {
        orderTableRepository.findById(event.getOrderTableId())
            .orElseThrow(() -> new IllegalArgumentException("테이블 정보가 없습니다."));
    }

}
