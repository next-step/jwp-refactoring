package kitchenpos.event.listener;

import java.util.Arrays;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.event.TableEmptyChangedEvent;
import kitchenpos.event.TableChangeEmptyEvent;
import kitchenpos.exception.OrderTableException;
import kitchenpos.repository.OrderRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class TableChangeEmptyEventListenerInOrder implements
    ApplicationListener<TableChangeEmptyEvent> {

    private final OrderRepository orderRepository;

    public TableChangeEmptyEventListenerInOrder(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void onApplicationEvent(TableChangeEmptyEvent event) {
        TableEmptyChangedEvent tableEmptyChangedEvent = (TableEmptyChangedEvent) event.getSource();
        validateOrderInTableIsComplete(tableEmptyChangedEvent.getOrderTableId());
    }

    private void validateOrderInTableIsComplete(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
            Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new OrderTableException("완료되지않은 주문이 있으면 상태변경을 할수 없습니다");
        }
    }
}
