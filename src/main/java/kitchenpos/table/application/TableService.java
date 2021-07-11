package kitchenpos.table.application;

import static kitchenpos.exception.KitchenposExceptionMessage.EXISTS_NOT_COMPLETION_ORDER;
import static kitchenpos.exception.KitchenposExceptionMessage.GUESTS_CANNOT_LOWER_THAN_MIN;
import static kitchenpos.exception.KitchenposExceptionMessage.NOT_FOUND_ORDER_TABLE;

import java.util.stream.Collectors;
import kitchenpos.exception.KitchenposException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableChangeEmptyRequest;
import kitchenpos.table.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TableService {

    private static final int MIN_NUMBER_OF_GUESTS = 0;

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        return OrderTableResponse.of(orderTableRepository.save(orderTableRequest.toOrderTable()));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                                   .stream()
                                   .map(OrderTableResponse::of)
                                   .collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);
        savedOrderTable.checkEmptyAndNotIncludeOrderTable();
        checkNotCompletionOrder(orderTableId);
        savedOrderTable.changeEmpty(request.isEmpty());
        return OrderTableResponse.of(savedOrderTable);
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                                   .orElseThrow(() -> new KitchenposException(NOT_FOUND_ORDER_TABLE));
    }

    private void checkNotCompletionOrder(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                                                                 OrderStatus.excludeCompletionList())) {
            throw new KitchenposException(EXISTS_NOT_COMPLETION_ORDER);
        }
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableChangeNumberOfGuestsRequest request) {
        checkNumberOfGuestsOverMin(request.getNumberOfGuests());
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.of(savedOrderTable);
    }

    public void checkNumberOfGuestsOverMin(final int numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
            throw new KitchenposException(GUESTS_CANNOT_LOWER_THAN_MIN);
        }
    }
}
