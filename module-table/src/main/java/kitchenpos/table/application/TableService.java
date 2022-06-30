package kitchenpos.table.application;

import kitchenpos.order.application.OrderStatusValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableChangEmptyRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final StatusValidator tableValidateInterface;

    public TableService(final OrderTableRepository orderTableRepository, OrderStatusValidator tableValidateInterface) {
        this.orderTableRepository = orderTableRepository;
        this.tableValidateInterface = tableValidateInterface;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableRequest.toOrderTable();

        return OrderTableResponse.of(saveOrderTable(orderTable));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return findOrderTables()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableChangEmptyRequest orderTableChangEmptyRequest) {
        final OrderTable savedOrderTable = findOrderTable(orderTableId);
        tableValidateInterface.validateOrderTableNotCompletion(orderTableId);

        savedOrderTable.updateEmpty(orderTableChangEmptyRequest.isEmpty());

        return OrderTableResponse.of(saveOrderTable(savedOrderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = findOrderTable(orderTableId);

        savedOrderTable.updateNumberOfGuests(orderTable.getNumberOfGuests());

        return OrderTableResponse.of(saveOrderTable(savedOrderTable));
    }

    private OrderTable saveOrderTable(OrderTable orderTable) {
        return orderTableRepository.save(orderTable);
    }

    private List<OrderTable> findOrderTables() {
        return orderTableRepository.findAll();
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(NoSuchElementException::new);
    }
}
