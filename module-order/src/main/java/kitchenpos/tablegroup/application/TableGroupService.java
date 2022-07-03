package kitchenpos.tablegroup.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.event.ReserveEvent;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(final TableGroupRepository tableGroupRepository,
                             final ApplicationEventPublisher applicationEventPublisher,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupValidator tableGroupValidator) {
        this.tableGroupRepository = tableGroupRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());
        applicationEventPublisher.publishEvent(
                new ReserveEvent(savedTableGroup.id(), tableGroupRequest.getOrderTables()));
        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        tableGroupValidator.validateComplete(orderTables.stream()
                .map(OrderTable::id)
                .collect(Collectors.toList()));
        orderTables.forEach(OrderTable::unGroup);
    }
}
