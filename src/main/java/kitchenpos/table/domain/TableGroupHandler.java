package kitchenpos.table.domain;

import kitchenpos.ExceptionMessage;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderValidator;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TableGroupHandler {

    private OrderTableRepository orderTableRepository;
    private OrderValidator orderValidator;

    public TableGroupHandler(OrderTableRepository orderTableRepository, OrderValidator orderValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderValidator = orderValidator;
    }

    @EventListener
    public void groupOrderTables(TableGroupedEvent event) {
        List<OrderTable> orderTableList = event.getOrderTableIds().stream()
                .map(id -> orderTableRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException(
                                ExceptionMessage.NOT_EXIST_ORDER_TABLE.getMessage())))
                .collect(Collectors.toList());

        OrderTables orderTables = new OrderTables(orderTableList);
        orderTables.group(event.getTableGroup());
        orderTableRepository.saveAll(orderTableList);
    }

    @EventListener
    public void ungroupOrderTables(TableUngroupedEvent event) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(event.getTableGroupId());
        for (OrderTable orderTable : orderTables) {
            orderValidator.checkCookingOrMeal(orderTable.getId());
            orderTable.ungroup();
        }
    }

}
