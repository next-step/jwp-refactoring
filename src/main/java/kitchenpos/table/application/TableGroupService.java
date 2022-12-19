package kitchenpos.table.application;

import kitchenpos.ExceptionMessage;
import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final ApplicationEventPublisher publisher;
    private final TableGroupRepository tableGroupRepository;
    private final TableService tableService;

    public TableGroupService(final ApplicationEventPublisher publisher, final TableGroupRepository tableGroupRepository,
                             final TableService tableService) {
        this.publisher = publisher;
        this.tableGroupRepository = tableGroupRepository;
        this.tableService = tableService;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final TableGroup tableGroup = new TableGroup();
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        publisher.publishEvent(new TableGroupedEvent(tableGroupRequest.getOrderTableIds(), savedTableGroup));
        List<OrderTable> orderTables = tableService.findAllByTableGroupId(savedTableGroup.getId());
        return TableGroupResponse.of(savedTableGroup, orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findById(tableGroupId);
        publisher.publishEvent(new TableUngroupedEvent(tableGroupId));
        tableGroupRepository.delete(tableGroup);
    }

    private TableGroup findById(Long id) {
        return tableGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.NOT_EXIST_TABLE_GROUP.getMessage()));
    }
}
