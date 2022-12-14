package kitchenpos.order.applicaiton;

import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderTableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderTableService(final OrderRepository orderRepository,
                             final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTable) {
        OrderTable entity = orderTable.toOrderTable();
        return OrderTableResponse.of(orderTableRepository.save(entity));
    }

    public OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderEmpty orderEmptyRequeset) {
        final OrderTable savedOrderTable = findOrderTable(orderTableId);
        validateOrderTable(savedOrderTable);
        savedOrderTable.updateEmpty(orderEmptyRequeset.isEmpty());
        return OrderTableResponse.of(savedOrderTable);
    }

    private void validateOrderTable(OrderTable savedOrderTable) {
        throwIfOrderTableIsInTableGroup(savedOrderTable);
        throwIfOrderTableInProgress(savedOrderTable);
    }

    private static void throwIfOrderTableIsInTableGroup(OrderTable savedOrderTable) {
        if (savedOrderTable.isInTableGroup()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderGuests changeGuestRequset) {
        final OrderTable savedOrderTable = findOrderTable(orderTableId);
        savedOrderTable.changeNumberOfGuests(changeGuestRequset.getNumberOfGuests());
        return OrderTableResponse.of(savedOrderTable);
    }

    private void throwIfOrderTableInProgress(OrderTable savedOrderTable) {
        if (orderRepository.existsByOrderTableAndOrderStatusIn(
                savedOrderTable, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
