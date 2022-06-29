package kitchenpos.table.application.handler;

import kitchenpos.order.domain.event.OrderTableExistCheckEvent;
import kitchenpos.order.exception.CreateOrderException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.exception.NotFoundOrderTableException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Transactional(readOnly = true)
@Component
public class OrderTableExistCheckEventHandler {

    private static final String ORDER_TABLE_IS_NOT_EMPTY = "주문 생성 시 주문테이블은 필수입니다.";

    private final OrderTableRepository orderTableRepository;

    public OrderTableExistCheckEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @TransactionalEventListener
    @Transactional
    public void handle(OrderTableExistCheckEvent event) {
        OrderTable orderTable = orderTableRepository.findById(event.getOrderTableId())
                .orElseThrow(() -> new NotFoundOrderTableException(event.getOrderTableId()));

        if (orderTable == null) {
            throw new CreateOrderException(ORDER_TABLE_IS_NOT_EMPTY);
        }
    }
}
