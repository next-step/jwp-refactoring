package kitchenpos.ordertable.event;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.event.OrderStatusChangedEvent;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.ordertable.dto.OrderTableIdRequest;
import kitchenpos.ordertable.exception.IllegalOrderTableEmptyChangeException;
import kitchenpos.ordertable.exception.IllegalOrderTableIdRequestException;
import kitchenpos.ordertable.exception.OrderStatusNotCompleteException;
import kitchenpos.tablegroup.event.TableGroupCreatedEvent;
import kitchenpos.tablegroup.event.TableGroupUnlinkEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Component
public class OrderTableEventHandler {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderTableEventHandler(OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Async
    @EventListener
    public void orderTableEmptyChange(OrderStatusChangedEvent event) {
        final OrderTable orderTable = orderTableRepository.findById(event.getOrderId())
                .orElseThrow(NoSuchElementException::new);

        orderTable.validateTableGroupNonNull();

        orderTable.changeEmpty(true);
        orderTable.unlinkTableGroup();
    }

    @Async
    @EventListener
    public void orderTablesEmptyChange(TableGroupCreatedEvent event) {
        List<OrderTableIdRequest> orderTableIdRequests = event.getOrderTableIdRequests();

        if (CollectionUtils.isEmpty(orderTableIdRequests) || orderTableIdRequests.size() < 2) {
            throw new IllegalOrderTableEmptyChangeException();
        }

        List<Long> orderTableIds = orderTableIdRequests.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());

        List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if(savedOrderTables.size() != orderTableIdRequests.size()) {
            throw new IllegalOrderTableIdRequestException();
        }

        for (OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.changeEmpty(false);
        }
    }

    @Async
    @EventListener
    public void orderTableUnlinkTableGroup(TableGroupUnlinkEvent event) {
        final List<OrderTable> orderTableList = orderTableRepository.findAllByTableGroupId(event.getTableGroupId());

        OrderTables orderTables = new OrderTables(orderTableList);
        List<Long> orderTableIds = orderTables.orderTableIds();

        if (orderRepository.existsByOrderTableInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new OrderStatusNotCompleteException();
        }

        for (final OrderTable orderTable : orderTableList) {
            orderTable.unlinkTableGroup();
        }
    }
}
