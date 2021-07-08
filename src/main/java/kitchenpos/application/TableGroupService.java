package kitchenpos.application;

import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.dto.order.TableGroupRequest;
import kitchenpos.event.order.TableGroupCreatedEvent;
import kitchenpos.event.order.TableOrderUngroupEvent;
import kitchenpos.exception.NotMatchOrderTableException;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderTableService orderTableService;
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher publisher;

    public TableGroupService(final OrderTableService orderTableService, final TableGroupRepository tableGroupRepository, ApplicationEventPublisher publisher) {
        this.orderTableService = orderTableService;
        this.tableGroupRepository = tableGroupRepository;
        this.publisher = publisher;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest tableGroupRequest) {

        List<OrderTable> orderTables = tableGroupRequest
                .getOrderTableRequests().stream()
                .map(orderTableRequest -> orderTableService.getOrderTable(orderTableRequest.getId()))
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

        final List<OrderTable> savedOrderTables = orderTableService.getAllOrderTablesByIds(orderTableIds);

        if (orderTables.size() != savedOrderTables.size()) {
            throw new NotMatchOrderTableException("orderTable size: " + orderTables.size() +
                    " savedOrderTables size: " + savedOrderTables.size());
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableService.getAllOrderTablesByGroupId(tableGroupId);

        publisher.publishEvent(new TableOrderUngroupEvent(orderTables));

        for (final OrderTable orderTable : orderTables) {
            orderTableService.makeTableGroupEmpty(orderTable);
        }
    }
}
