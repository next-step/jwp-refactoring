package kitchenpos.order.application;

import static kitchenpos.common.exception.ExceptionMessage.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.TableGroupSavedEvent;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import kitchenpos.order.repository.TableGroupRepository;

@Service
public class TableGroupService {

    private final TableService tableService;
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TableGroupService(TableService tableService,
        TableGroupRepository tableGroupRepository, ApplicationEventPublisher eventPublisher) {
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTables();
        final List<OrderTable> findOrderTables = tableService.findByOrderTableIds(orderTableIds);
        final TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.of());

        eventPublisher.publishEvent(new TableGroupSavedEvent(savedTableGroup,
            findOrderTables.stream().map(OrderTable::getId).collect(Collectors.toList())));
        return new TableGroupResponse(savedTableGroup, findOrderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup findTableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_DATA));

        // findTableGroup.validateUngroup();
        // findTableGroup.ungroup();
        tableGroupRepository.delete(findTableGroup);
    }
}
