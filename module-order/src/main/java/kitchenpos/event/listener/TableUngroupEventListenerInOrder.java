package kitchenpos.event.listener;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.event.TableUngroupedEvent;
import kitchenpos.event.TableUngroupEvent;
import kitchenpos.exception.TableGroupException;
import kitchenpos.repository.OrderRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class TableUngroupEventListenerInOrder implements ApplicationListener<TableUngroupEvent> {

    private final OrderRepository orderRepository;

    public TableUngroupEventListenerInOrder(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void onApplicationEvent(TableUngroupEvent event) {
        TableUngroupedEvent tableUngroupedEvent = (TableUngroupedEvent) event.getSource();

        checkAllMenuIsCompleteInTableGroup(tableUngroupedEvent.getOrderTableIds());
    }

    private void checkAllMenuIsCompleteInTableGroup(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new TableGroupException("테이블에 완료가 안된 주문이 있습니다");
        }
    }
}
