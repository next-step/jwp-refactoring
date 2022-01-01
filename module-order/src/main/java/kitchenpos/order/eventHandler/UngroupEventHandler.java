package kitchenpos.order.eventHandler;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.OrderTables;
import kitchenpos.tableGroup.event.UngroupEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class UngroupEventHandler {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public UngroupEventHandler(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    public void onUngroupEvent(UngroupEvent event) {
        final OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroupId(event.getTableGroupId()));

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTables.getOrderTableIds(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("테이블이 식사중이거나 조리중이면 단체 지정을 해제할 수 없습니다.");
        }

        orderTables.ungroup();
    }
}
