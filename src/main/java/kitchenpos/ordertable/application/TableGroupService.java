package kitchenpos.ordertable.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.domain.TableGroup;
import kitchenpos.ordertable.domain.TableGroupRepository;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.TableGroupRequest;
import kitchenpos.ordertable.dto.TableGroupResponse;
import kitchenpos.ordertable.event.GroupEvent;
import kitchenpos.ordertable.event.GroupInfo;
import kitchenpos.ordertable.event.UngroupEvent;
import kitchenpos.ordertable.exception.TableGroupNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TableGroupService(TableGroupRepository tableGroupRepository,
        ApplicationEventPublisher applicationEventPublisher) {
        this.tableGroupRepository = tableGroupRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTableRequest> orderTableRequests = tableGroupRequest.getOrderTables();
        List<Long> orderTableIds = extractOrderTableIds(orderTableRequests);

        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());
        GroupEvent groupEvent = new GroupEvent(this,
            new GroupInfo(savedTableGroup.getId(), orderTableIds));
        applicationEventPublisher.publishEvent(groupEvent);
        return TableGroupResponse.from(savedTableGroup);
    }

    private List<Long> extractOrderTableIds(List<OrderTableRequest> orderTableRequests) {
        return orderTableRequests.stream()
            .map(OrderTableRequest::getId)
            .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findTableGroup(tableGroupId);
        UngroupEvent ungroupEvent = new UngroupEvent(this, tableGroup.getId());
        applicationEventPublisher.publishEvent(ungroupEvent);
    }

    public TableGroup findTableGroup(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
            .orElseThrow(TableGroupNotFoundException::new);
    }
}
