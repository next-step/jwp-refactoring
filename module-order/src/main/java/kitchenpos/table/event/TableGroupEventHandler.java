package kitchenpos.table.event;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.Orders;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.event.TableGroupUpdateEvent;
import kitchenpos.tablegroup.event.TableUnGroupEvent;

@Component
public class TableGroupEventHandler {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableGroupEventHandler(final OrderTableRepository orderTableRepository,
        final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Async
    @EventListener
    public void updateTableGroup(final TableGroupUpdateEvent event) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(event.getOrderTablesIds());
        final TableGroup tableGroup = event.getTableGroup();
        orderTables.forEach(orderTable -> orderTable.updateTableGroup(tableGroup.getId()));
        orderTableRepository.saveAll(orderTables);
    }

    @Async
    @EventListener
    public void ungroup(final TableUnGroupEvent tableUnGroupEvent) {
        final OrderTables orderTables = new OrderTables(findAllOrderTable(tableUnGroupEvent));
        final List<Order> orderList = findAllOrder(orderTables.ids());
        orderTables.ungroup(new Orders(orderList));
    }

    private List<OrderTable> findAllOrderTable(final TableUnGroupEvent tableUnGroupEvent) {
        return orderTableRepository.findAllByTableGroupId(tableUnGroupEvent.getTableGroupId());
    }

    private List<Order> findAllOrder(final List<Long> orderTableIds) {
        return orderRepository.findAllByOrderTableIdIn(orderTableIds);
    }
}
