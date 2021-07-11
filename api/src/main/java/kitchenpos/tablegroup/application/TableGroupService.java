package kitchenpos.tablegroup.application;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.event.GroupCreatedEvent;
import kitchenpos.common.event.UnGroupedEvent;
import kitchenpos.error.InvalidRequestException;
import kitchenpos.error.NotFoundTableGroup;
import kitchenpos.order.repository.OrderTableDao;
import kitchenpos.tablegroup.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.repository.TableGroupDao;

@Service
@Transactional
public class TableGroupService {
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;
    private final ApplicationEventPublisher eventPublisher;

    public TableGroupService(final OrderTableDao orderTableDao, final TableGroupDao tableGroupDao, final  ApplicationEventPublisher eventPublisher) {
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
        this.eventPublisher = eventPublisher;
    }

    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.ids();

        final TableGroup tableGroup = new TableGroup();
        tableGroupDao.save(tableGroup);

        final OrderTables orderTables = OrderTables.of(tableGroup, orderTableDao.findAllById(orderTableIds));

        if (orderTables.size() != orderTableIds.size()) {
            throw new InvalidRequestException();
        }

        eventPublisher.publishEvent(new GroupCreatedEvent(orderTableIds, tableGroup));

        return TableGroupResponse.of(tableGroup);
    }

    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupDao.findById(tableGroupId).orElseThrow(NotFoundTableGroup::new);
        eventPublisher.publishEvent(new UnGroupedEvent(tableGroup));
    }
}
