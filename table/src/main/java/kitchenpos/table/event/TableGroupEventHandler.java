package kitchenpos.table.event;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.table.event.TableGroupValidator.*;

@Component
public class TableGroupEventHandler {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableGroupEventHandler(OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Async
    @EventListener
    public void savedOrderTables(TableGroupCreatedEvent event) {
        validOrderTablesSize(event.getOrderTableIdRequests());
        List<OrderTable> orderTables = findOrderTable(event.getOrderTableRequests());
        validGroupOrderTableList(orderTables);
        for (OrderTable orderTable: orderTables) {
            orderTable.group(event.tableGroup);
        }
    }

    @Async
    @EventListener
    public void ungroupOrderTables(TableGroupUngroupEvent event) {
        List<OrderTable> orderTableList = orderTableRepository.findAllByTableGroup(event.getTableGroupId());
        for (OrderTable orderTable: orderTableList) {
            Order order = orderRepository.findByOrderTableId(orderTable.getId());
            if (!order.isOrderStatusCompletion()) {
                throw new IllegalArgumentException("주문 테이블의 주문 상태가 조리 또는 식사인 경우 테이블을 비울 수 없습니다.");
            }
            orderTable.unGroup();
        }
    }

    private List<OrderTable> findOrderTable(List<OrderTableRequest> orderTableRequests) {
        final List<Long> orderTableIds = orderTableRequests.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
        List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        validRegisteredOrderTable(orderTables.size(), orderTableIds.size());
        return orderTableRepository.findAllById(orderTableIds);
    }
}
