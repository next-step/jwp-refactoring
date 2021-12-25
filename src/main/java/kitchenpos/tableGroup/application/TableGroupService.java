package kitchenpos.tableGroup.application;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.domain.TableGroupRepository;
import kitchenpos.tableGroup.dto.TableGroupRequest;
import kitchenpos.tableGroup.dto.TableGroupResponse;
import kitchenpos.order.event.OrderTableGroupEvent;
import kitchenpos.order.event.OrderTableUngroupEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final TableGroupValidator tableGroupValidator;
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TableGroupService(final TableGroupValidator tableGroupValidator,
                             final TableGroupRepository tableGroupRepository,
                             final ApplicationEventPublisher eventPublisher) {
        this.tableGroupValidator = tableGroupValidator;
        this.tableGroupRepository = tableGroupRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public TableGroupResponse create(TableGroupRequest request) {
        final List<OrderTable> orderTables = tableGroupValidator.getOrderTable(request.getOrderTables());
        final TableGroup saved = tableGroupRepository.save(TableGroup.empty());
        eventPublisher.publishEvent(OrderTableGroupEvent.from(saved.getId(), orderTables));
        return TableGroupResponse.of(saved, orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);

        eventPublisher.publishEvent(OrderTableUngroupEvent.from(tableGroup));

        tableGroupRepository.delete(tableGroup);
    }


}
