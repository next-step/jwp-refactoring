package kitchenpos.table.application;

import kitchenpos.exception.OrderTableException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.publisher.TableEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderTableService {

    private static final String NOT_FOUND_ORDER_TABLE_ERROR_MESSAGE = "등록 되지 않은 주문 테이블 입니다.";

    private final OrderTableRepository orderTableRepository;
    private final TableEventPublisher tableEventPublisher;

    public OrderTableService(OrderTableRepository orderTableRepository, TableEventPublisher tableEventPublisher) {
        this.orderTableRepository = orderTableRepository;
        this.tableEventPublisher = tableEventPublisher;
    }

    public OrderTableResponse create(final OrderTableRequest request) {
        OrderTable orderTable = request.toOrderTable();
        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream().map(OrderTableResponse::of).collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = this.findById(orderTableId);
        tableEventPublisher.changeEmptyValidPublishEvent(orderTable);
        orderTable.changeEmpty(orderTableRequest.isEmpty());
        return OrderTableResponse.of(orderTable);
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = this.findById(orderTableId);
        orderTable.checkIsEmpty();
        orderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
        return OrderTableResponse.of(orderTable);
    }

    private OrderTable findById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new OrderTableException(NOT_FOUND_ORDER_TABLE_ERROR_MESSAGE));
    }

    public OrderTables findAllByIds(List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        return new OrderTables(orderTables);

    }

    public OrderTables findAllByTableGroupId(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroup(tableGroupId);
        return new OrderTables(orderTables);
    }

    public OrderTable findOrderTable(Long orderTableId) {
        return findById(orderTableId);
    }
}
