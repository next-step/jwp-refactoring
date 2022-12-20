package kitchenpos.table.application;

import kitchenpos.exception.ErrorMessage;
import kitchenpos.table.domain.OrderEmpty;
import kitchenpos.table.domain.OrderGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TableService {
    private final TableValidator tableValidator;
    private final OrderTableRepository orderTableRepository;

    public TableService(TableValidator tableValidator, OrderTableRepository orderTableRepository) {
        this.tableValidator = tableValidator;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(OrderTableRequest request) {
        return OrderTableResponse.of(orderTableRepository.save(request.toOrderTable()));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return OrderTableResponse.list(orderTableRepository.findAll());
    }

    public OrderTableResponse changeEmpty(Long orderTableId, OrderEmpty request) {
        OrderTable savedOrderTable = findOrderTableById(orderTableId);
        tableValidator.validateChangeEmpty(savedOrderTable);

        savedOrderTable.changeEmpty(request.isEmpty());

        return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.ORDER_TABLE_NOT_FOUND_BY_ID.getMessage()));
    }

    public OrderTableResponse changeNumberOfGuests(Long orderTableId, OrderGuests request) {
        OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }
}
