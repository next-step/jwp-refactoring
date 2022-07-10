package table.application;

import table.domain.OrderTable;
import table.dto.OrderTableRequest;
import table.dto.OrderTableResponse;
import table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Transactional(readOnly = true)
@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final TableValidator tableValidator;

    public TableService(OrderTableRepository orderTableRepository, TableValidator tableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        return OrderTableResponse.of(orderTableRepository.save(request.toOrderTable()));
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.of(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(Long orderTableId, Boolean empty) {
        OrderTable orderTable = findById(orderTableId);
        tableValidator.validateOrderTable(orderTable);
        orderTable.changeEmpty(empty);
        return OrderTableResponse.of(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(Long orderTableId, Integer numberOfGuests) {
        OrderTable orderTable = findById(orderTableId);
        orderTable.changeNumberOfGuests(numberOfGuests);
        return OrderTableResponse.of(orderTable);
    }

    public OrderTable findById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }
}
