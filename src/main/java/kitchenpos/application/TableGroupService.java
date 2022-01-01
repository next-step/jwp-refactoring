package kitchenpos.application;

import kitchenpos.common.exceptions.EmptyOrderTableGroupException;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final TableService tableService;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TableGroupService(final TableService tableService, final TableGroupValidator tableGroupValidator, final TableGroupRepository tableGroupRepository
            , final ApplicationEventPublisher applicationEventPublisher) {
        this.tableService = tableService;
        this.tableGroupValidator = tableGroupValidator;
        this.tableGroupRepository = tableGroupRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        tableGroupValidator.validateOrderTables(request);
        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());

        final List<OrderTable> orderTables = tableService.findAllById(request.getOrderTableIds());
        applicationEventPublisher.publishEvent(TableEvent.Grouped.from(savedTableGroup, orderTables));
        return TableGroupResponse.of(savedTableGroup, orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findByTableGroupId(tableGroupId);
        applicationEventPublisher.publishEvent(TableEvent.Ungrouped.from(tableGroup.getId()));
        tableGroupRepository.delete(tableGroup);
    }

    private TableGroup findByTableGroupId(final Long id) {
        return tableGroupRepository.findById(id)
                .orElseThrow(EmptyOrderTableGroupException::new);
    }
}
