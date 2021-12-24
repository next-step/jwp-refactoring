package kitchenpos.tablegroup.application;

import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupDto;
import kitchenpos.tablegroup.event.GroupingOrderTableEvent;
import kitchenpos.tablegroup.event.UngroupOrderTableEvent;
import kitchenpos.tablegroup.domain.TableGroupValidator;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;
    private final ApplicationEventPublisher eventPublisher;
    
    public TableGroupService(
        final TableGroupRepository tableGroupRepository,
        final TableGroupValidator tableGroupValidator,
        final ApplicationEventPublisher eventPublisher
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public TableGroupDto create(final TableGroupDto tableGroupDto) {
        final OrderTables validatedOrderTables = tableGroupValidator.getValidatedOrderTables(tableGroupDto);
        final List<Long> orderTableIds = validatedOrderTables.getOrderTableIds();
        
        TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.of());
        
        eventPublisher.publishEvent(new GroupingOrderTableEvent(savedTableGroup.getId(), orderTableIds));

        return TableGroupDto.of(savedTableGroup, validatedOrderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        OrderTables validatedOrderTables = tableGroupValidator.getComplateOrderTable(tableGroupId);
        final List<Long> orderTableIds = validatedOrderTables.getOrderTableIds();

        eventPublisher.publishEvent(new UngroupOrderTableEvent(tableGroupId, orderTableIds));
    }
}
