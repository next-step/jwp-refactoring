package kitchenpos.tablegroup.application;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.tablegroup.domain.OrderTable;
import kitchenpos.tablegroup.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableEmpty;
import kitchenpos.tablegroup.domain.TableGuests;
import kitchenpos.tablegroup.dto.OrderTableRequest;
import kitchenpos.tablegroup.dto.OrderTableResponse;
import kitchenpos.tablegroup.exception.OrderTableExceptionCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {
    private final TableValidator tableValidator;
    private final OrderTableRepository orderTableRepository;

    public TableService(TableValidator tableValidator, OrderTableRepository orderTableRepository) {
        this.tableValidator = tableValidator;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(OrderTableRequest request) {
        return OrderTableResponse.of(orderTableRepository.save(request.toOrderTable()));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return OrderTableResponse.list(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(Long orderTableId, TableEmpty request) {
        OrderTable savedOrderTable = findOrderTableById(orderTableId);
        tableValidator.validateToChangeEmpty(savedOrderTable);

        savedOrderTable.changeEmpty(request.isEmpty());

        return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new EntityNotFoundException(OrderTableExceptionCode.NOT_FOUND_BY_ID.getMessage()));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(Long orderTableId, TableGuests request) {
        OrderTable savedOrderTable = findOrderTableById(orderTableId);
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
    }
}
