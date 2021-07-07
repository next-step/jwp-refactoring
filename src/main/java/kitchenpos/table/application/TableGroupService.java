package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(OrderTableDao orderTableDao, TableGroupDao tableGroupDao) {
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> savedOrderTables = findOrderTables(tableGroupRequest.toOrderTables());
        return TableGroupResponse.of(tableGroupDao.save(new TableGroup(new OrderTables(savedOrderTables))));
    }

    private List<OrderTable> findOrderTables(List<OrderTable> orderTables) {
        List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
        return orderTableDao.findAllById(orderTableIds);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupDao.findById(tableGroupId)
            .orElseThrow(EntityNotFoundException::new);
        tableGroup.upgroup();
    }
}
