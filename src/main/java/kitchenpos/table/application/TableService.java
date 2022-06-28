package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.NumberOfGuestsRequest;
import kitchenpos.table.dto.OrderTableEmptyRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        OrderTable orderTable = orderTableRepository.save(request.toOrderTable());
        orderTableRepository.flush();
        return OrderTableResponse.of(orderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableEmptyRequest request) {
        final OrderTable savedOrderTable = findByIdOrElseThrow(orderTableId);

        savedOrderTable.validate();
        validateOrderTableIdAndOrderStatusIn(orderTableId);

        savedOrderTable.changeEmpty(request.isEmpty());

        return OrderTableResponse.of(savedOrderTable);
    }

    private void validateOrderTableIdAndOrderStatusIn(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final NumberOfGuestsRequest request) {
        final OrderTable savedOrderTable = findByIdOrElseThrow(orderTableId);
        savedOrderTable.validateOrderTableEmpty();
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return OrderTableResponse.of(savedOrderTable);
    }

    private OrderTable findByIdOrElseThrow(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
