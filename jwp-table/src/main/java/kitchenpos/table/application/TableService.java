package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.exception.EmptyOrderTableException;
import kitchenpos.table.exception.MinimumOrderTableNumberException;
import kitchenpos.table.exception.NotEmptyOrderTableGroupException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableService {
    private static final int MINIMUM = 2;

    private final OrderTableRepository orderTableRepository;
    private  final TableValidator tableValidator;

    public TableService(final OrderTableRepository orderTableRepository, final TableValidator tableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        final OrderTable orderTable = orderTableRepository.save(request.toOrderTable());
        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = getNotGroupedOrderTableById(orderTableId);
        tableValidator.validateEmpty(orderTable.getId());

        orderTable.changeEmptyStatus(orderTableRequest.isEmpty());
        return OrderTableResponse.from(orderTable);
    }

    private OrderTable getNotGroupedOrderTableById(final Long id) {
        final OrderTable orderTable = findById(id);
        if (Objects.nonNull(orderTable.getTableGroupId())) {
            throw new NotEmptyOrderTableGroupException();
        }
        return orderTable;
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable orderTable = findById(orderTableId);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTable> findAllById(final List<Long> ids) {
        return orderTableRepository.findAllById(ids);
    }

    public void grouped(final Long tableGroupId, final List<Long> orderTableIds) {
        final List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        validateOrderTables(orderTables);
        tableValidator.validateGroup(orderTables);
        for (OrderTable orderTable : orderTables) {
            orderTable.group(tableGroupId);
        }
        orderTableRepository.saveAll(orderTables);
    }

    public void ungrouped(final Long tableGroupId) {
        final List<OrderTable> tables = orderTableRepository.findByTableGroupId(tableGroupId);
        tableValidator.validateUnGroup(tables);
        for (OrderTable table : tables) {
            table.unGroup();
        }
        orderTableRepository.saveAll(tables);
    }

    private void validateOrderTables(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables)) {
            throw new EmptyOrderTableException();
        }
        if (orderTables.size() < MINIMUM) {
            throw new MinimumOrderTableNumberException();
        }
        if (hasAnyEmptyTable(orderTables)) {
            throw new NotEmptyOrderTableGroupException();
        }
    }

    private OrderTable findById(final Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(EmptyOrderTableException::new);
    }

    private static boolean hasAnyEmptyTable(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .anyMatch(OrderTable::isEmpty);
    }
}
