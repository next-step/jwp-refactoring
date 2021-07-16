package kitchenpos.application.table;


import static kitchenpos.exception.KitchenposExceptionMessage.NOT_FOUND_ORDER_TABLE;

import java.util.stream.Collectors;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.table.OrderTableChangeEmptyRequest;
import kitchenpos.dto.table.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.dto.table.OrderTableRequest;
import kitchenpos.dto.table.OrderTableResponse;
import kitchenpos.exception.KitchenposException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderValidator orderValidator;

    public TableService(final OrderTableRepository orderTableRepository, final OrderValidator orderValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderValidator = orderValidator;
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
        orderValidator.checkNotCompletionOrder(orderTableId);
        savedOrderTable.changeEmpty(request.isEmpty());
        return OrderTableResponse.of(savedOrderTable);
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                                   .orElseThrow(() -> new KitchenposException(
                                       NOT_FOUND_ORDER_TABLE));
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableChangeNumberOfGuestsRequest request) {
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.of(savedOrderTable);
    }
}
