package kitchenpos.table.application;

import kitchenpos.common.exception.InvalidOrderStatusException;
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

        checkTableOrderStatus(savedOrderTable);
        savedOrderTable.changeEmpty(changeEmptyRequest.isEmpty());

        return OrderTableResponse.of(savedOrderTable);
    }

    private void checkTableOrderStatus(OrderTable orderTable) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new InvalidOrderStatusException();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final ChangeGuestsRequest changeGuestsRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findByIdElseThrow(orderTableId);
        savedOrderTable.changeNumberOfGuests(changeGuestsRequest.toNumberOfGuests());
        return OrderTableResponse.of(savedOrderTable);
    }

    public boolean isExistsByIdAndEmptyTrue(final Long id) {
        return orderTableRepository.existsByIdAndEmptyTrue(id);
    }

    public boolean isExistsById(Long id) {
        return orderTableRepository.existsById(id);
    }
}
