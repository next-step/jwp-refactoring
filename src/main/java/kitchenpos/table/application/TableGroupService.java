package kitchenpos.table.application;

import java.util.List;
import kitchenpos.common.exception.NoResultDataException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableDao;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.domain.TableGroupDao;
import kitchenpos.tableGroup.domain.TableGroupValidation;
import kitchenpos.tableGroup.dto.TableGroupRequest;
import kitchenpos.tableGroup.dto.TableGroupResponse;
import kitchenpos.table.domain.event.GroupByEvent;
import kitchenpos.table.domain.event.GroupTable;
import kitchenpos.table.domain.event.UnGroupByEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;
    private final ApplicationEventPublisher eventPublisher;
    private final TableGroupValidation tableGroupValidation;

    public TableGroupService(
        OrderTableDao orderTableDao,
        TableGroupDao tableGroupDao,
        ApplicationEventPublisher eventPublisher,
        TableGroupValidation tableGroupValidation) {
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
        this.eventPublisher = eventPublisher;
        this.tableGroupValidation = tableGroupValidation;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(tableGroupRequest.getOrderTableIds());

        tableGroupValidation.valid(orderTables, tableGroupRequest.getOrderTables());

        TableGroup savedTableGroup = tableGroupDao.save(TableGroup.of());

        groupByEvent(tableGroupRequest, savedTableGroup);

        return TableGroupResponse.of(savedTableGroup);
    }

    private void groupByEvent(TableGroupRequest tableGroupRequest, TableGroup savedTableGroup) {
        GroupByEvent groupByEvent = new GroupByEvent(this,
            new GroupTable(
                savedTableGroup.getId(),
                tableGroupRequest.getOrderTableIds()
            )
        );

        eventPublisher.publishEvent(groupByEvent);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupDao.findById(tableGroupId)
            .orElseThrow(NoResultDataException::new);
        UnGroupByEvent unGroupByEvent = new UnGroupByEvent(this, tableGroup.getId());
        eventPublisher.publishEvent(unGroupByEvent);
    }
}
