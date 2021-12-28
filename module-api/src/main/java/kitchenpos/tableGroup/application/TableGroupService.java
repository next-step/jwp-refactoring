package kitchenpos.tableGroup.application;

import kitchenpos.order.application.TableService;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.domain.TableGroupRepository;
import kitchenpos.tableGroup.dto.OrderTableIdRequest;
import kitchenpos.tableGroup.dto.TableGroupRequest;
import kitchenpos.tableGroup.dto.TableGroupResponse;
import kitchenpos.order.event.OrderTableGrouped;
import kitchenpos.order.event.OrderTableUngrouped;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final TableGroupValidator tableGroupValidator;
    private final TableGroupRepository tableGroupRepository;
    private final TableService tableService;
    private final ApplicationEventPublisher eventPublisher;

    public TableGroupService(TableGroupValidator tableGroupValidator,
                             TableGroupRepository tableGroupRepository,
                             TableService tableService,
                             ApplicationEventPublisher eventPublisher) {
        this.tableGroupValidator = tableGroupValidator;
        this.tableGroupRepository = tableGroupRepository;
        this.tableService = tableService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public TableGroupResponse create(TableGroupRequest request) {
        final List<Long> orderTableIds = request.getOrderTables().stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
        final List<OrderTable> orderTables = tableService.findAllByIdIn(orderTableIds);
        tableGroupValidator.validateOrderTableSize(request.getOrderTables(), orderTables.size());

        final TableGroup saved = tableGroupRepository.save(TableGroup.empty());

        eventPublisher.publishEvent(OrderTableGrouped.from(saved.getId(), orderTableIds));

        return TableGroupResponse.of(saved, orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);
        tableGroupRepository.delete(tableGroup);

        eventPublisher.publishEvent(OrderTableUngrouped.from(tableGroupId));
    }


}
