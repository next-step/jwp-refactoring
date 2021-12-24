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
import kitchenpos.order.domain.TableGroupValidator;
import kitchenpos.order.domain.TableUngroupEvent;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import kitchenpos.order.repository.TableGroupRepository;

@Service
public class TableGroupService {

    private final TableService tableService;
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(TableService tableService,
        TableGroupRepository tableGroupRepository, ApplicationEventPublisher eventPublisher,
        TableGroupValidator tableGroupValidator) {
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
        this.eventPublisher = eventPublisher;
        this.tableGroupValidator = tableGroupValidator;
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
        tableGroupValidator.validateUngroup(findTableGroup.getId());
        tableGroupRepository.delete(findTableGroup);

        eventPublisher.publishEvent(new TableUngroupEvent(findTableGroup));
    }
}
