package kitchenpos.table.domain;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.exception.KitchenposErrorCode;
import kitchenpos.common.exception.KitchenposException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.tablegroup.domain.UngroupingTableEvent;

@Component
public class UngroupingTableEventHandler {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public UngroupingTableEventHandler(OrderTableRepository orderTableRepository,
        OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    @EventListener
    public void handle(UngroupingTableEvent event) {
        List<OrderTable> tables = orderTableRepository.findAllByTableGroupId(event.getTableGroupId());

        OrderTables orderTables = new OrderTables(tables);

        checkContainsCookingOrMealTable(orderTables.getOrderTables());

        orderTables.unGroup();
    }

    private void checkContainsCookingOrMealTable(List<OrderTable> orderTables) {
        if (orderRepository.existsByOrderTableInAndOrderStatusIn(
            orderTables, OrderStatus.NOT_COMPLETED_LIST)) {
            throw new KitchenposException(KitchenposErrorCode.CONTAINS_USED_TABLE);
        }
    }
}
