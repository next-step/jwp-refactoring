package kitchenpos.order.event.tableGroup;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.repository.OrderTableRepository;
import kitchenpos.order.dto.OrderTableRequest;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.order.event.tableGroup.TableGroupValidator.*;

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
        List<OrderTable> orderTableList = orderTableRepository.findAllByTableGroupId(event.getTableGroupId());
        for (OrderTable orderTable: orderTableList) {
            Order order = orderRepository.findByOrderTaleId(orderTable.getId());
            if (!order.isOrderStatusCompletion()) {
                throw new IllegalArgumentException("주문 테이블의 주문 상태가 조리 또는 식사인 경우 테이블을 비울 수 없습니다.");
            }
            orderTable.unGroup();
        }
    }

//    private void validOrderTablesSize(List<OrderTableRequest> orderTables) {
//        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
//            throw new IllegalArgumentException("그룹테이블은 2개 이상이어야 그룹화가 가능합니다.");
//        }
//    }
//
//    private void validGroupOrderTableList(List<OrderTable> orderTables) {
//        for (final OrderTable orderTable : orderTables) {
//            orderTable.validGroupOrderTable();
//        }
//    }

    private List<OrderTable> findOrderTable(List<OrderTableRequest> orderTableRequests) {
        final List<Long> orderTableIds = orderTableRequests.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
        List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        validRegisteredOrderTable(orderTables.size(), orderTableIds.size());
        return orderTableRepository.findAllById(orderTableIds);
    }

//    private void validRegisteredOrderTable(int orderTableSize, int orderTableIdSize) {
//        if (orderTableSize != orderTableIdSize) {
//            throw new IllegalArgumentException("등록된 주문테이블만 그룹화 할 수 있습니다.");
//        }
//    }
}
