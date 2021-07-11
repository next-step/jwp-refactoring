package kitchenpos.ordertable.event;

import kitchenpos.order.event.OrderStatusChangedEvent;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.dto.OrderTableIdRequest;
import kitchenpos.ordertable.exception.IllegalOrderTableEmptyChangeException;
import kitchenpos.tablegroup.event.TableGroupCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Component
public class OrderTableEventHandler {
    private final OrderTableRepository orderTableRepository;

    public OrderTableEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    public void orderTableEmptyChange(OrderStatusChangedEvent event) {
        final OrderTable orderTable = orderTableRepository.findById(event.getOrderId())
                .orElseThrow(NoSuchElementException::new);

        orderTable.validateTableGroupNonNull();

        orderTable.changeEmpty(true);
        orderTable.unlinkTableGroup();
    }

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

        for (OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.changeEmpty(false);
        }
    }
}
