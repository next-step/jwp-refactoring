package kitchenpos.event;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class TableGroupUnGroupEventHandler {

    @EventListener
    public void ungroup(TableGroupUnGroupEvent event) {

        List<OrderTable> orderTables = event.getOrderTables();

        orderTables.forEach(orderTable -> {
            orderTable.getOrders().forEach(Order::checkOrderStatusCookingOrMeal);
            orderTable.unGroup();
        });
    }

}
