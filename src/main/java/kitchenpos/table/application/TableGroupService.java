package kitchenpos.table.application;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import kitchenpos.orders.dao.OrderDao;
import kitchenpos.orders.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao, final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTable> orderTables = orderTableDao.findAllByIdIn(tableGroupRequest.getOrderTableIds());
        final TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(orderTables));
        return TableGroupResponse.of(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupDao.findById(tableGroupId).orElseThrow(IllegalArgumentException::new);
        tableGroup.ungroup();
    }
}
