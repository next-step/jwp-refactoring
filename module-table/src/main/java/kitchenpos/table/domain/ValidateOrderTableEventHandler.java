package kitchenpos.table.domain;

import kitchenpos.order.domain.OrderCreateEvent;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ValidateOrderTableEventHandler {

    private final OrderTableRepository orderTableRepository;

    public ValidateOrderTableEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    public void handle(OrderCreateEvent event) {
        OrderTable orderTable = findOrderTable(event.getOrderTableId());
        if (orderTable.isEmptyTable()) {
            throw new IllegalArgumentException("[ERROR] 빈테이블인 경우 주문을 등록 할 수 없습니다.");
        }
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 테이블이 등록되어있지 않습니다."));
    }

}
