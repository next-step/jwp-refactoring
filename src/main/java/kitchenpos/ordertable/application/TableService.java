package kitchenpos.ordertable.application;

import kitchenpos.exception.CannotFindException;
import kitchenpos.ordertable.event.TableEmptyStatusChangedEvent;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.common.Message.ERROR_ORDER_TABLE_NOT_FOUND;

@Service
@Transactional
public class TableService {
    private final ApplicationEventPublisher eventPublisher;
    private final OrderTableRepository orderTableRepository;

    public TableService(final ApplicationEventPublisher eventPublisher, final OrderTableRepository orderTableRepository) {
        this.eventPublisher = eventPublisher;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        return OrderTableResponse.of(orderTableRepository.save(toOrderTable(orderTableRequest)));
    }

    private OrderTable toOrderTable(OrderTableRequest orderTableRequest) {
        return new OrderTable(orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty());
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return toOrderTableResponses(orderTableRepository.findAll());
    }

    private List<OrderTableResponse> toOrderTableResponses(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);
        eventPublisher.publishEvent(new TableEmptyStatusChangedEvent(orderTableRequest));
        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());
        return OrderTableResponse.of(savedOrderTable);
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new CannotFindException(ERROR_ORDER_TABLE_NOT_FOUND));
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);
        savedOrderTable.updateNumberOfGuestsTo(orderTableRequest.getNumberOfGuests());
        return OrderTableResponse.of(savedOrderTable);
    }
}
