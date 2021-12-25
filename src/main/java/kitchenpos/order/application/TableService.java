package kitchenpos.order.application;

import kitchenpos.order.application.exception.TableNotFoundException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        OrderTable save = orderTableRepository.save(request.toEntity());
        return OrderTableResponse.of(save);
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return OrderTableResponse.ofList(orderTables);
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long tableId) {
        final OrderTable orderTable = getTable(tableId);
        orderTable.changeEmpty();
        return OrderTableResponse.of(orderTable);
    }

    @Transactional
    public OrderTableResponse changeGuests(final Long tableId, final OrderTableRequest request) {
        final OrderTable orderTable = getTable(tableId);
        orderTable.changeGuests(request.getNumberOfGuests());
        return OrderTableResponse.of(orderTable);
    }

    private OrderTable getTable(Long tableId) {
        return orderTableRepository.findById(tableId)
                .orElseThrow(TableNotFoundException::new);
    }

    @Transactional
    public OrderTableResponse changeOrderStatus(Long tableId, OrderStatusRequest request) {
        final OrderTable orderTable = getTable(tableId);
        orderTable.changeStatus(request.getOrderStatus());
        return OrderTableResponse.of(orderTable);
    }
}
