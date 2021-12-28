package kitchenpos.tablegroup.domain;

import kitchenpos.global.exception.EntityNotFoundException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.exception.TableGroupNotAvailableException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class TableGroupValidator {

    private final OrderTableRepository orderTableRepository;

    public TableGroupValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void createValidator(final TableGroupCreateRequest request) {
        List<Long> tableIds = request.getOrderTables().stream()
                .map(TableGroupCreateRequest.OrderTable::getId)
                .collect(Collectors.toList());
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(tableIds);
        existCheckOrderTable(savedOrderTables, request);
        checkAvailabilityTableGroup(savedOrderTables);
    }

    private void checkAvailabilityTableGroup(List<OrderTable> orderTables) {
        orderTables.stream().forEach(orderTable -> {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new TableGroupNotAvailableException(String.format("table id is %d", orderTable.getId()));
            }
        });

    }

    private void existCheckOrderTable(final List<OrderTable> savedOrderTables, final TableGroupCreateRequest request) {
        if (savedOrderTables.size() != request.getOrderTables().size()) {
            throw new EntityNotFoundException("orderTable Not Found");
        }
    }
}
