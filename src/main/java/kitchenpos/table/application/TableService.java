package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.ChangeEmptyRequest;
import kitchenpos.table.dto.ChangeGuestsRequest;
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

    public TableService(
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        return OrderTableResponse.of(orderTableRepository.save(
                orderTableRequest.toOrderTable()
        ));
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final ChangeEmptyRequest changeEmptyRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findByIdElseThrow(orderTableId);

        checkTableGroupIsNull(savedOrderTable);
        checkTableOrderStatus(savedOrderTable);

        savedOrderTable.changeEmpty(changeEmptyRequest.isEmpty());

        return OrderTableResponse.of(savedOrderTable);
    }

    private void checkTableOrderStatus(OrderTable orderTable) {
        if (orderRepository.existsByOrderTableAndOrderStatusIn(
                orderTable, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    private void checkTableGroupIsNull(OrderTable orderTable) {
        if (orderTable.isNotNullTableGroup()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final ChangeGuestsRequest changeGuestsRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findByIdElseThrow(orderTableId);
        checkEmptyOrderTable(savedOrderTable);

        savedOrderTable.changeNumberOfGuests(changeGuestsRequest.toNumberOfGuests());

        return OrderTableResponse.of(savedOrderTable);
    }

    private void checkEmptyOrderTable(OrderTable savedOrderTable) {
        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
