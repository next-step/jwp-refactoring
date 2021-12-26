package kitchenpos.tablegroup.application;

import static kitchenpos.common.exception.ExceptionMessage.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupSavedEvent;
import kitchenpos.tablegroup.domain.TableGroupValidator;
import kitchenpos.tablegroup.domain.TableUngroupEvent;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.repository.TableGroupRepository;

@Service
public class TableGroupService {

    private final TableGroupValidator tableGroupValidator;
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TableGroupService(TableGroupValidator tableGroupValidator,
        TableGroupRepository tableGroupRepository, ApplicationEventPublisher eventPublisher) {
        this.tableGroupValidator = tableGroupValidator;
        this.tableGroupRepository = tableGroupRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTables();
        final List<OrderTable> findOrderTables = tableGroupValidator.validateOrderTables(orderTableIds);
        final TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.of());

        List<Long> findOrderTableIds = findOrderTables.stream().map(OrderTable::getId)
            .collect(Collectors.toList());
        eventPublisher.publishEvent(new TableGroupSavedEvent(savedTableGroup, findOrderTableIds));
        return new TableGroupResponse(savedTableGroup, findOrderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup findTableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_DATA));
        tableGroupValidator.validateUngroup(findTableGroup.getId());
        tableGroupRepository.delete(findTableGroup);

        eventPublisher.publishEvent(new TableUngroupEvent(findTableGroup));
    }
}
