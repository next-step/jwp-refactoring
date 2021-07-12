package kitchenpos.domain.table;

import kitchenpos.dto.table.TableGroupRequest;
import kitchenpos.event.table.TableGroupCreatedEvent;
import kitchenpos.event.table.TableOrderUngroupEvent;
import kitchenpos.exception.table.NotMatchOrderTableException;
import kitchenpos.repository.table.TableGroupRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final TableService tableService;
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher publisher;

    public TableGroupService(final TableService tableService, final TableGroupRepository tableGroupRepository, ApplicationEventPublisher publisher) {
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
        this.publisher = publisher;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest tableGroupRequest) {

        List<OrderTable> orderTables = tableGroupRequest
                .getOrderTableRequests().stream()
                .map(orderTableRequest -> tableService.getOrderTable(orderTableRequest.getId()))
                .collect(Collectors.toList());

        validateSizeOfOrderTables(orderTables);

        TableGroup tableGroup = TableGroup.of(orderTables);
        publisher.publishEvent(new TableGroupCreatedEvent(tableGroup));

        return tableGroupRepository.save(tableGroup);
    }

    private void validateSizeOfOrderTables(List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = tableService.getAllOrderTablesByIds(orderTableIds);

        if (orderTables.size() != savedOrderTables.size()) {
            throw new NotMatchOrderTableException("orderTable size: " + orderTables.size() +
                    " savedOrderTables size: " + savedOrderTables.size());
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = tableService.getAllOrderTablesByGroupId(tableGroupId);

        publisher.publishEvent(new TableOrderUngroupEvent(orderTables));

        for (final OrderTable orderTable : orderTables) {
            tableService.makeTableGroupEmpty(orderTable);
        }
    }
}
