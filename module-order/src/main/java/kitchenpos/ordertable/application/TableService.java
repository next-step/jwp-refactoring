package kitchenpos.ordertable.application;

import kitchenpos.common.OrderErrorCode;
import kitchenpos.exception.NotFoundException;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.TableValidator;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final TableValidator tableValidator;

    public TableService(
        final OrderTableRepository orderTableRepository,
        final TableValidator tableValidator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableRequest.toOrderTable();
        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    @Transactional
    public OrderTableResponse changeEmpty(
        final Long orderTableId,
        final OrderTableRequest orderTable
    ) {
        OrderTable savedOrderTable = findOrderTableById(orderTableId);

        savedOrderTable.changeEmpty(tableValidator, orderTable.isEmpty());

        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(
        final Long orderTableId,
        final OrderTableRequest orderTable
    ) {
        OrderTable savedOrderTable = findOrderTableById(orderTableId);

        savedOrderTable.changeNumberOfGuests(orderTable.getNumberOfGuests());
        return OrderTableResponse.of(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.toList(orderTableRepository.findAll());
    }

    public OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new NotFoundException(OrderErrorCode.ORDER_NOT_FOUND_EXCEPTION));
    }
}
