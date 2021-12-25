package kitchenpos.order.application;

import kitchenpos.table.application.TableService;
import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroupUnGroupEvent;
import kitchenpos.order.repository.OrderRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class TableGroupUnGroupEventHandler {

    private final OrderRepository orderRepository;
    private final TableService tableService;

    public TableGroupUnGroupEventHandler(OrderRepository orderRepository, TableService tableService) {
        this.orderRepository = orderRepository;
        this.tableService = tableService;
    }


    @EventListener
    public void unGroupOrderTables(TableGroupUnGroupEvent event) {

        Long tableGroupId = event.getTableGroupId();
        List<OrderTable> orderTables = tableService.getOrderTablesByTableGroup(tableGroupId);

        orderTables.forEach(orderTable -> {
            List<Order> orders = orderRepository.findAllByOrderTableId(orderTable.getId());
            orders.forEach(Order::checkOrderStatusCookingOrMeal);
            orderTable.unGroup();
        });
    }

}
