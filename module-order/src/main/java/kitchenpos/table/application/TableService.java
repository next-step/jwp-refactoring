package kitchenpos.table.application;

import kitchenpos.table.domain.Empty;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.ChangeEmptyRequest;
import kitchenpos.table.dto.ChangeNumberOfGuestsRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Transactional(readOnly = true)
@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final TableValidator tableValidator;

    public TableService(
            OrderTableRepository orderTableRepository
            , TableValidator tableValidator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        OrderTable orderTable = request.toOrderTable();

        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    public List<OrderTableResponse> list() {
        final List<OrderTable> persistOrderTables = orderTableRepository.findAll();

        return OrderTableResponse.fromList(persistOrderTables);
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final ChangeEmptyRequest request) {
        OrderTable persistOrderTable = findById(orderTableId);
        Empty empty = Empty.of(request.isEmpty());

        persistOrderTable.changeEmpty(empty, tableValidator);
        return OrderTableResponse.of(persistOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final ChangeNumberOfGuestsRequest request) {
        OrderTable persistOrderTable = findById(orderTableId);
        NumberOfGuests numberOfGuests = NumberOfGuests.of(request.getNumberOfGuests());

        persistOrderTable.changeNumberOfGuests(numberOfGuests, tableValidator);
        return OrderTableResponse.of(persistOrderTable);
    }

    public OrderTable findById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(NoSuchElementException::new);
    }

    public List<OrderTable> findAllByIdIn(List<Long> orderTableIds) {
        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return orderTableRepository.findAllByTableGroupId(tableGroupId);
    }

    public boolean existsById(Long orderTableId) {
        return orderTableRepository.existsById(orderTableId);
    }
}
