package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        return OrderTableResponse.from(orderTableRepository.save(
                new OrderTable(null, orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty())));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        List<OrderTable> tables = orderTableRepository.findAll();
        return tables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = findOrderTable(orderTableId);
        validateAlreadyOrdered(orderTableId);
        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());
        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }

    private void validateAlreadyOrdered(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable savedOrderTable = findOrderTable(orderTableId);
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }

    private OrderTable findOrderTable(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        return orderTable;
    }
}
