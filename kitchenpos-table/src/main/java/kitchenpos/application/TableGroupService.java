package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.exception.NoSuchDataException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher publisher;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository,
                             final ApplicationEventPublisher publisher) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.publisher = publisher;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTableList = tableGroupRequest.getOrderTableIds().stream()
                .map(this::findOrderTableById)
                .collect(Collectors.toList());

        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());
        final OrderTables orderTables = new OrderTables(orderTableList);
        orderTables.groupTables(savedTableGroup);

        return TableGroupResponse.of(savedTableGroup, orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup persistTableGroup = findTableGroupById(tableGroupId);
        final OrderTables orderTables = findOrderTablesByTableGroupId(persistTableGroup.getId());
        orderTables.getOrderTables().stream()
                .forEach(orderTable -> {
                    publisher.publishEvent(new TableUngroupedEvent(orderTable.getId()));
                    orderTable.unGroup();
                });
        }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId).orElseThrow(NoSuchDataException::new);
    }

    private OrderTables findOrderTablesByTableGroupId(Long tableGroupId) {
        return new OrderTables(orderTableRepository.findOrderTablesByTableGroupId(tableGroupId));
    }

    private TableGroup findTableGroupById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId).orElseThrow(NoSuchDataException::new);
    }
}
