package kitchenpos.table.application;

import kitchenpos.ExceptionMessage;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher publisher;

    public TableService(final OrderTableRepository orderTableRepository, final ApplicationEventPublisher publisher) {
        this.orderTableRepository = orderTableRepository;
        this.publisher = publisher;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        final NumberOfGuests guestNumber = new NumberOfGuests(orderTableRequest.getNumberOfGuests());
        final OrderTable orderTable = new OrderTable(guestNumber, orderTableRequest.getEmpty());
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return OrderTableResponse.of(savedOrderTable);
    }

    public List<OrderTableResponse> findAll() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    public OrderTable findById (Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.NOT_EXIST_ORDER_TABLE.getMessage()));
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, boolean empty) {
        final OrderTable savedOrderTable = findById(orderTableId);
        publisher.publishEvent(new TableEmptyChangedEvent(orderTableId));
        savedOrderTable.changeEmpty(empty);
        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, int numberOfGuests) {
        final OrderTable savedOrderTable = findById(orderTableId);
        final NumberOfGuests newNumberOfGuests = new NumberOfGuests(numberOfGuests);
        savedOrderTable.changeNumberOfGuests(newNumberOfGuests);
        return OrderTableResponse.of(savedOrderTable);
    }

    public List<OrderTable> findAllByTableGroupId(Long id) {
        return orderTableRepository.findAllByTableGroupId(id);
    }

    @Transactional
    public OrderTables createGroupedOrderTables(List<Long> orderTableIds, TableGroup tableGroup) {
        List<OrderTable> orderTableList = orderTableIds.stream()
                .map(id -> orderTableRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException(
                                ExceptionMessage.NOT_EXIST_ORDER_TABLE.getMessage())))
                .collect(Collectors.toList());

        OrderTables orderTables = new OrderTables(orderTableList);
        orderTables.group(tableGroup);
        orderTableRepository.saveAll(orderTableList);
        return orderTables;
    }

    @Transactional
    public void ungroupOrderTables(Long tableGroupId) {
        final List<OrderTable> orderTableList = orderTableRepository.findAllByTableGroupId(tableGroupId);
        final List<Long> orderTableIds = orderTableList.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        publisher.publishEvent(new TableUngroupedEvent(orderTableIds));
        orderTableList.forEach(OrderTable::ungroup);
    }
}
