package kitchenpos.orders.table.application;

import java.util.NoSuchElementException;
import kitchenpos.orders.order.event.OrderCreatedEvent;
import kitchenpos.orders.table.domain.OrderTable;
import kitchenpos.orders.table.domain.OrderTableRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CreateOrderTableValidationHandler {

    private final OrderTableRepository orderTableRepository;

    public CreateOrderTableValidationHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    public void validateOrderTable(OrderCreatedEvent event) {
        OrderTable orderTable = orderTableRepository.findById(event.getOrderTableId())
                .orElseThrow(() -> new NoSuchElementException("주문 테이블이 시스템에 없습니다."));
        if (orderTable.isEmptyTable()) {
            throw new IllegalArgumentException("빈 주문 테이블을 지정하셨습니다.");
        }
    }
}
