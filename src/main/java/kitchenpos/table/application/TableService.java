package kitchenpos.table.application;

import kitchenpos.common.exception.ErrorCode;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final TableStatusService tableStatusService;

    public TableService(OrderTableRepository orderTableRepository, TableStatusService tableStatusService) {
        this.orderTableRepository = orderTableRepository;
        this.tableStatusService = tableStatusService;
    }

    public OrderTableResponse create(OrderTableRequest request) {
        OrderTable orderTable = request.toEntity(request);
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return OrderTableResponse.of(orderTableRepository.findAll());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_TABLE_NOT_FOUND));

        tableStatusService.validateOrderTableStatus(orderTableId);

        savedOrderTable.changeEmpty(request.isEmpty());

        return OrderTableResponse.of(savedOrderTable);
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_TABLE_NOT_FOUND));

        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return OrderTableResponse.of(savedOrderTable);
    }
}
