package kitchenpos.service.table.application;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.service.table.dto.OrderTableRequest;
import kitchenpos.service.table.dto.OrderTableResponse;
import kitchenpos.service.table.dto.OrderTableUpdateEmptyRequest;
import kitchenpos.service.table.dto.OrderTableUpdateNumberOfGuestsRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final TableValidator tableValidator;

    public TableService(final OrderTableRepository orderTableRepository, final TableValidator tableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        OrderTable orderTable = OrderTable.of(request.getNumberOfGuests(), request.isEmpty());
        orderTableRepository.save(orderTable);
        return new OrderTableResponse(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream().map(OrderTableResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableUpdateEmptyRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId).orElseThrow(NoSuchElementException::new);
        tableValidator.validateChangeEmpty(savedOrderTable);
        savedOrderTable.updateEmpty(request.isEmpty());
        return new OrderTableResponse(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableUpdateNumberOfGuestsRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId).orElseThrow(NoSuchElementException::new);
        tableValidator.validateChangeNumberOfGuests(savedOrderTable, request);
        savedOrderTable.updateNumberOfGuests(request.getNumberOfGuests());
        return new OrderTableResponse(savedOrderTable);
    }
}
