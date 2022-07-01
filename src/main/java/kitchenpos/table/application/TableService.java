package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.Empty;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static kitchenpos.common.Messages.ORDER_TABLE_CANNOT_EMPTY;
import static kitchenpos.common.Messages.ORDER_TABLE_STATUS_CANNOT_UPDATE;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableService(OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        NumberOfGuests numberOfGuests = NumberOfGuests.of(orderTableRequest.getNumberOfGuests());
        Empty empty = Empty.of(orderTableRequest.isEmpty());

        OrderTable orderTable = orderTableRepository.save(OrderTable.of(numberOfGuests, empty));
        return OrderTableResponse.of(orderTable);
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream().map(OrderTableResponse::of).collect(Collectors.toList());
    }

    @Transactional
    public ChangeEmptyResponse changeEmpty(final Long orderTableId, final ChangeEmptyRequest changeEmptyRequest) {
        OrderTable orderTable = findById(orderTableId);
        orderTable.validateHasOrderTable();

        if (orderRepository.existsByOrderTableAndOrderStatusIn(
                orderTable,
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))
        ) {
            throw new IllegalArgumentException(ORDER_TABLE_STATUS_CANNOT_UPDATE);
        }

        orderTable.setEmpty(Empty.of(changeEmptyRequest.isEmpty()));
        return ChangeEmptyResponse.of(orderTableRepository.save(orderTable));
    }

    @Transactional
    public ChangeNumberOfGuestsResponse changeNumberOfGuests(
            final Long orderTableId,
            final ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest
    ) {
        NumberOfGuests numberOfGuests = NumberOfGuests.of(changeNumberOfGuestsRequest.getNumberOfGuests());
        OrderTable orderTable = findById(orderTableId);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(ORDER_TABLE_CANNOT_EMPTY);
        }

        orderTable.setNumberOfGuests(numberOfGuests);
        return ChangeNumberOfGuestsResponse.of(orderTableRepository.save(orderTable));
    }

    public OrderTable findById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(NoSuchElementException::new);
    }

    public List<OrderTable> findByIdIn(List<Long> ids) {
        return orderTableRepository.findByIdIn(ids);
    }
}
