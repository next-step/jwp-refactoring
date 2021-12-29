package kitchenpos.order.application;

import kitchenpos.order.application.event.TableGroupingEvent;
import kitchenpos.order.application.event.TableUnGroupingEvent;
import kitchenpos.order.application.exception.TableGroupNotFoundException;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import kitchenpos.order.repository.TableGroupRepository;
import kitchenpos.order.repository.TableRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final TableRepository tableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;
    private final ApplicationEventPublisher eventPublisher;

    public TableGroupService(final TableRepository tableRepository,
                             final TableGroupRepository tableGroupRepository,
                             final TableGroupValidator tableGroupValidator,
                             final ApplicationEventPublisher eventPublisher) {
        this.tableRepository = tableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<Long> tableIds = request.getTableIds();
        tableGroupValidator.validate(tableIds);

        final TableGroup persistTableGroup = tableGroupRepository.save(new TableGroup());
        eventPublisher.publishEvent(new TableGroupingEvent(persistTableGroup.getId(), tableIds));

        return TableGroupResponse.of(persistTableGroup, tableIds);
    }

    @Transactional
    public void ungroup(final Long id) {
        TableGroup tableGroup = getTableGroup(id);
        List<OrderTable> orderTables = tableRepository.findByTableGroupId(tableGroup.getId());
        tableGroupValidator.validateTableState(orderTables);

        eventPublisher.publishEvent(new TableUnGroupingEvent(tableGroup.getId(), getTableIds(orderTables)));
        tableGroupRepository.delete(tableGroup);
    }

    private List<Long> getTableIds(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    private TableGroup getTableGroup(Long id) {
        return tableGroupRepository.findById(id)
                .orElseThrow(TableGroupNotFoundException::new);
    }
}
